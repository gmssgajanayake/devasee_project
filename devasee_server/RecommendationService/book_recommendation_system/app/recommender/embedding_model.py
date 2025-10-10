from sentence_transformers import SentenceTransformer
import numpy as np

model = SentenceTransformer('all-MiniLM-L6-v2')
print("### Model is loading...")

# Function to create weighted embeddings
def get_weighted_embedding(row):
        
    # Define weights
    weights = {
        'author': 1.0,
        'publisher': 0.5,
        'category': 3.0,
        'genres': 3.0,
        'description': 1.5,
        'keywords': 2.0
    }
        
     # convert lists to strings
    parts = {
        'author': " ".join(row['author']),
        'publisher': " ".join(row['publisher']),
        'category': " ".join(row['category']),
        'genres': " ".join(row['genres']),
        'description': " ".join(row['description']),
        'keywords': " ".join(row['keywords'])
    }

    final_embedding = np.zeros(model.get_sentence_embedding_dimension())

    for key, text in parts.items():
        if text.strip(): # skip empty
            emb = model.encode(text)
            final_embedding += weights[key] * emb
    return final_embedding