# recommendation Engine Core Logic is herte like
#   - fwetching books data from spring-boot backend 
#   - retraining recommendation model
#   - at app start load pkls into memory

import joblib
import pandas as pd
import numpy as np
from difflib import get_close_matches
from app.models import BookResponse
import requests
from tqdm import tqdm
from sklearn.metrics.pairwise import cosine_similarity
import random
import os

from app.recommender.data_cleaning_functions import (
    genres_string_to_list_convertor, 
    keywords_string_to_list_convertor, 
    remove_spaces, 
    lowercase_words, 
    to_list
)

from app.recommender.embedding_model import get_weighted_embedding
from app.recommender.loader import save_and_upload, load_data_from_blob


book_df = None
similarity_matrix = None

book_blob_name = "book_df.pkl"
sim_blob_name = "similarity_matrix.pkl"

local_book_path = 'app/artifacts_v1/book_df.pkl'
local_sim_path = 'app/artifacts_v1/similarity_matrix.pkl'

# Get all books
SPRING_BOOKS_URL = "http://api.devasee.lk/api/v1/product/books/all"
# SPRING_BOOKS_URL = "http://localhost:8080/api/v1/product/books/all"


# ---------------------- Retrainning --------------------------


# Fetching book data from springboot, create data frame
def fetch_books_from_spring():
    print(f"### Fetching books data from {SPRING_BOOKS_URL}")

    try:
        response = requests.get(SPRING_BOOKS_URL, timeout=10)
        response.raise_for_status()
        books_json = response.json()
        books_data = books_json['data'] 
        return pd.json_normalize(books_data, sep='_')
    except requests.RequestException as e:
        print(f"### Error fetching books: {e}")
        return pd.DataFrame()  # return empty df instead of crashing



# Retrain similarity matrix from Spring Boot data"
def retrain_model():
    global book_df, similarity_matrix
    print("### Retraining model from Spring Boot backend...")

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
    
    # Generate embeddings

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

    save_and_upload(book_df, similarity_matrix, book_blob_name, sim_blob_name)
    print("### Retraining completed & pkl uploaded to Azure/local !")
    return book_df, similarity_matrix



# ---------------------- Recommendation --------------------------



# Initial loading of data
def initial_pkl_loader():
    global book_df, similarity_matrix

    print("### initial_pkl_loader")
    try:
        # if local data exists load data from local else load from azure
        if os.path.exists(local_book_path) and os.path.exists(local_sim_path):
            book_df = joblib.load(local_book_path)
            similarity_matrix = joblib.load(local_sim_path)
        else:
            book_df, similarity_matrix = load_data_from_blob(book_blob_name, sim_blob_name)
            if book_df is None or similarity_matrix is None:
                print("### Pickles missing in both local, azure, retraining model...")
                book_df, similarity_matrix = retrain_model()

        print(f"### Pkls loaded into memory , books: {book_df.shape[0]}")
        print(book_df.head())
        print(book_df.columns.tolist())
    except Exception as e:
        print(f"### Pkls laoding error into memory{e}")



# title into lower then find, then retun original
def get_best_match(book_title, titles):
    titles_lower = [t.lower() for t in titles]
    matches = get_close_matches(book_title.lower(), titles_lower, n=1, cutoff=0.6)
    if matches:
        # Return the original title with correct case
        return [titles[titles_lower.index(m)] for m in matches]
    return []  # empty list if no match

# loading saved pkls into memory at startup
initial_pkl_loader()


def ensure_data_loaded():
    global book_df, similarity_matrix
    if book_df is None or similarity_matrix is None:
        print("### Data missing, retraining model...")
        book_df, similarity_matrix = retrain_model()   # retrain_model returns both

        

# Recommendation function
def get_recommendation(book_title: str, top_n: int=10):
    
    # Ensure data was loaded
    ensure_data_loaded()
     
    # fetch latest Spring data if not loaded yet
    global book_df, similarity_matrix

    titles = book_df['title'].tolist()
    print(f"### Exsiting title list len : {len(titles)}")
    for t in titles:
        print(f"- {t}")

    # Use fuzzy matching
    # matches = get_close_matches(book_title, titles, n=1, cutoff=0.6)
    matches = get_best_match(book_title, titles)
    
    if not matches:
        print(f"No close match found for '{book_title}', returning random books")
        random_books = book_df.sample(n=min(top_n, len(book_df)))
        results = [BookResponse(title=row['title'], isbn=str(row['isbn']), score=round(random.random(), 3)) for _, row in random_books.iterrows()]
        return results
    
    best_match = matches[0]
    print(f"### best match {best_match}")

    # Finds the row number of the movie in new_df whose title exactly matches best_match
    index = book_df[book_df['title']==best_match].index[0]
    
    distances = similarity_matrix[index]

    #  [0.2, 0.8, 0.5] ---> [(0, 0.2), (1, 0.8), (2, 0.5)], 
    # list() converts that enumeration object into a list of tuples,
    distance_pairs = list(enumerate(distances)) 
    distance_pairs = sorted(distance_pairs, key=lambda x: x[1], reverse=True)
    
    results = []
    print(f"\n### Results for: {best_match}\n")

    for i, score in distance_pairs[1:top_n+1]:
        book = book_df.iloc[i]
        bookResponse = BookResponse(title=book['title'], isbn=str(book['isbn']), score=round(score, 3))
        results.append(bookResponse)
        print(f"### {book['title']},  {book['isbn']} (Similarity: {score:.3f})")
    return results
