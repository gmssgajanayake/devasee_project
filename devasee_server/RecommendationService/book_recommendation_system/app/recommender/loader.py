from azure.storage.blob import BlobServiceClient
import joblib
import io
import pandas as pd

# # TODO unocmment all
# # Azure Blob configuration
# AZURE_CONN_STR = "<your_connection_string>"
# BLOB_CONTAINER = "book-data"
# BOOK_DF_BLOB = "book_df.pkl"
# SIMMILARITY_MATRIX_BLOB = "similarity_matrix.pkl"

# # client object that represents storage account
# blob_service_client =  BlobServiceClient.from_connection_string(AZURE_CONN_STR)
# # client object for a specific container in that storage account.
# container_client = blob_service_client.get_container_client(BLOB_CONTAINER)

# Global variables
book_df = None
similarity_matrix = None

# # Donaload blob by the name
# def load_blob_file(blob_name: str):
#     blob_client = container_client.get_blob_client(blob_name) # creates a client for a specific blob
#     try:
#         stream = blob_client.download_blob() # starts the download
#         return stream.readall() # reads all bytes into memory
#     except Exception as e:
#         print(f"Error downloading {blob_name}: {e}")
#         return None

# def load_data_from_blob():
#     global book_df, similarity_matrix
    
#     book_bytes = load_blob_file(BOOK_DF_BLOB) # downloads the book_df.pkl bytes from Azure
#     sim_bytes = load_blob_file(SIMMILARITY_MATRIX_BLOB) # A .pkl file isn’t text, it contains serialized Python objects so download as bytes

#     if not book_bytes or not sim_bytes:
#         print("Blob data not found. Run retrain_model() first.")
#         return
    
#     # updating global variables
#     book_df = joblib.load(io.BytesIO(book_bytes)) # converts bytes to a Python object (pandas.DataFrame)
#     similarity_matrix = joblib.load(io.BytesIO(sim_bytes)) # converts bytes to a NumPy array
#     print("Data loaded from Azure Blob successfully!")

# # Upload updated book_df, similarity_matrix to azure
# def upload_to_blob(local_path: str, blob_name: str):
#     blob_client = container_client.get_blob_client(blob_name)
#     with open(local_path, "rb") as data:
#         blob_client.upload_blob(data, overwrite=True)
#     print(f" Uploaded {blob_name} to Azure Blob Storage.")

# TODO : Testing save to local
def save_to_local():
    try:
        joblib.dump(book_df, 'app/artifacts_v1/book_df.pkl')
        joblib.dump(similarity_matrix, 'app/artifacts_v1/similarity_matrix.pkl')
    except Exception as e:
        print("Error saving pkl into local")