import joblib
import pandas as pd
import numpy as np
from difflib import get_close_matches
from app.models import BookResponse
from threading import Lock
import requests
from tqdm import tqdm
from sklearn.metrics.pairwise import cosine_similarity

from app.recommender.data_cleaning_functions import (
    genres_string_to_list_convertor, 
    keywords_string_to_list_convertor, 
    remove_spaces, 
    lowercase_words, 
    to_list
)

from app.recommender.embedding_model import get_weighted_embedding
# from app.recommender.loader import upload_to_blob # todo

lock = Lock()

book_df = None
similarity_matrix = None

#Get all books
# SPRING_BOOKS_URL = "http://localhost:8080/api/v1/product/books/all"
SPRING_BOOKS_URL = "http://localhost:8080/api/v1/product/books/all"


# ---------------------- Retrainning --------------------------


# Fetching book data from springboot, create data frame
def fetch_books_from_spring():
    response = requests.get(SPRING_BOOKS_URL)
    response.raise_for_status()
    books_json = response.json()
    books_data = books_json['data'] 
    df = pd.json_normalize(books_data, sep='_') 
    return df



# Retrain similarity matrix from Spring Boot data"
def retrain_model():
    global book_df, similarity_matrix
    print("Retraining model from Spring Boot backend...")

    book_df = fetch_books_from_spring()

    # Data cleaning
    for col in book_df:
        if book_df[col].apply(lambda x : isinstance(x, list)).any():
            book_df[col] = book_df[col].apply(lambda x : str(x))

    # remove duplicate rows
    if book_df.duplicated(subset='title', keep=False).sum() > 0:
        book_df = book_df.drop_duplicates(subset='title', keep='first')
        print("Duplicates found!, cleaned success")
    

    # remove null values
    if book_df.isnull().values.any() > 0:
        book_df = book_df.dropna()
        print("Missing values found!, cleaned")

    # Convert into list, lowercasing, space removing
    book_df['genres'] = book_df['genres'].apply(genres_string_to_list_convertor).apply(remove_spaces).apply(lowercase_words)
    book_df['keywords'] = book_df['keywords'].apply(keywords_string_to_list_convertor).apply(remove_spaces).apply(lowercase_words)
    book_df['description'] = book_df['description'].apply(lambda x : x.split() if isinstance(x, str) else x).apply(lowercase_words)
    book_df['author'] = book_df['author'].apply(to_list).apply(remove_spaces).apply(lowercase_words)
    book_df['publisher']  = book_df['publisher'].apply(to_list).apply(remove_spaces).apply(lowercase_words)
    book_df['category']  = book_df['category'].apply(to_list).apply(remove_spaces).apply(lowercase_words)

    # empty list to hold embeddings
    embedding_list = []

    # Iterate over rows with a progress bar
    for _, row  in tqdm(book_df.iterrows(), total=len(book_df), desc="Embedding books"):
        # Get the embedding for this row
        emb = get_weighted_embedding(row)
        # Append to our list
        embedding_list.append(emb)
    
    # Convert final list into a NumPy array
    embeddings = np.array(embedding_list)  
    # Cosine similarity
    similarity_matrix = cosine_similarity(embeddings)

    # Save pickles locally (or upload to Azure Blob)
    with lock:
        # TODO : For testing local
        joblib.dump(book_df, 'artifacts_v1/book_df_new.pkl')
        joblib.dump(similarity_matrix, 'artifacts_v1/similarity_matrix_new.pkl')

        # TODO : azure
        #upload_to_blob('artifacts_v1/book_df.pkl', 'book_df.pkl')
        #upload_to_blob('artifacts_v1/similarity_matrix.pkl', 'similarity_matrix.pkl')

    print("### Retraining completed & pkl uploaded to Azure/local !")




# ---------------------- Recommendation --------------------------

# todo if pkl not there retrain model
def initial_pkl_loader():
    global book_df, similarity_matrix
    try:
        # TODO : testing
        book_df = joblib.load("app/artifacts_v1/book_df.pkl")
        similarity_matrix = joblib.load("app/artifacts_v1/similarity_matrix.pkl")

        # azure
        # TODO :  get from azure

        print("### Pkls loaded into memory")
        print(book_df.head())
    except Exception as e:
        print(f"### Pkls laoding error into memory{e}")



# loading saved pkls into memory at startup
initial_pkl_loader()

# Recommendation function
def get_recommendation(book_title: str, top_n: int=10):

    # fetch latest Spring data if not loaded yet
    global book_df, similarity_matrix

    if book_df is None or similarity_matrix is None:
        print("### book_df or simmilarity_matrix is none retraining triggering")
        # retrain_model()
        
    titles = book_df['title'].tolist()

    print(f"Title list len : {len(titles)}")

    if book_title not in titles:
        print("book_title not in titles")
        return []

    matches = get_close_matches(book_title, titles, n=1, cutoff=0.6)
    
    if not matches:
        print(f"No close match found for '{book_title}'")
        return
    
    best_match = matches[0]
    print(f"best match {best_match}")

    # Finds the row number of the movie in new_df whose title exactly matches best_match
    index = book_df[book_df['title']==best_match].index[0]
    
    distances = similarity_matrix[index]

    #  [0.2, 0.8, 0.5] ---> [(0, 0.2), (1, 0.8), (2, 0.5)], 
    # list() converts that enumeration object into a list of tuples,
    distance_pairs = list(enumerate(distances)) 
    distance_pairs = sorted(distance_pairs, key=lambda x: x[1], reverse=True)
    
    results = []
    print(f"\nResults for: {best_match}\n")

    for i, score in distance_pairs[1:top_n+1]:
        book = book_df.iloc[i]
        bookResponse = BookResponse(title=book['title'], score=round(score, 3))
        results.append(bookResponse)
        print(f"{book['title']} (Similarity: {score:.3f})")
    return results
