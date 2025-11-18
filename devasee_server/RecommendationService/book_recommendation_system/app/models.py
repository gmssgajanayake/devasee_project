from pydantic import BaseModel

# with BaseModel : 
# Automatically check and validate that incoming JSON has those fields
# Reject invalid or missing data with an automatic 422 error
# Convert the JSON body into a ready-to-use Python object

class RecommendationRequest(BaseModel):
    title: str
    top_n: int = 10

class BookResponse(BaseModel):
    title: str
    score: float
