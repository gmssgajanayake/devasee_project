import joblib
import io
import pandas as pd
from azure.storage.blob import BlobServiceClient
from threading import Lock

from app.recommender.azure_config import AZURE_CONNECTION_STRING, AZURE_CONTAINER_NAME

lock = Lock()

# Global variables
book_df = None
similarity_matrix = None



# -------------------- Azure Blob Utilities --------------------

def get_container_client():
    blob_service_client = BlobServiceClient.from_connection_string(AZURE_CONNECTION_STRING)
    return blob_service_client.get_container_client(AZURE_CONTAINER_NAME)

# Upload local pkls files into azure blob, overwriting if exists
def upload_to_blob(local_file_path: str, blob_name: str): 
    container_client = get_container_client()

    with open(local_file_path, "rb") as data:
        container_client.upload_blob(name=blob_name, data=data, overwrite=True)
    print(f"### Uploaded {blob_name} to Azure Blob Storage")

# Download a blob and return its content as bytes
def download_blob_to_bytes(blob_name: str):
    container_client = get_container_client()
    blob_client = container_client.get_blob_client(blob_name)
    try:
        stream = blob_client.download_blob()
        return stream.readall()
    except Exception as e:
        print(f"### Error downloading {blob_name}: {e}")
        return None


# -------------------- Load/Save Pickles --------------------

# Load book_df and similarity_matrix directly from Azure Blob
def load_data_from_blob(book_blob_name: str, sim_blob_name: str):

    book_bytes = download_blob_to_bytes(book_blob_name)
    sim_bytes = download_blob_to_bytes(sim_blob_name)

    if book_bytes and sim_bytes:
        book_df = joblib.load(io.BytesIO(book_bytes))
        similarity_matrix = joblib.load(io.BytesIO(sim_bytes))
        print("### pkl are exits in blob storage")
    else:
        print("### Blob data not found. Please retrain and upload first.")
        return None, None
    print(f"### Data loaded successfully. Books: {book_df.shape[0]}")
    return book_df, similarity_matrix  # return both

# Save locally and upload pickles to Azure, thread-safe
def save_and_upload(book_df_obj, sim_matrix_obj, book_blob_name: str, sim_blob_name: str):
    local_book_path = 'app/artifacts_v1/book_df.pkl'
    local_sim_path = 'app/artifacts_v1/similarity_matrix.pkl'

    try:
        with lock:  
            # Save locally
        
            joblib.dump(book_df_obj, local_book_path)
            joblib.dump(sim_matrix_obj, local_sim_path)
            print("### Saved pickles locally successfully")

            # Upload to Azure
            upload_to_blob(local_book_path, book_blob_name)
            upload_to_blob(local_sim_path, sim_blob_name)
            print("### Pickles uploaded to Azure successfully")
    except Exception as e:
        print(f"### Error saving/uploading pickles: {e}")
        raise RuntimeError("Failed to save/upload pickles") from e

        