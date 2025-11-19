from fastapi import FastAPI, HTTPException
from app.models import RecommendationRequest, BookResponse
from app.recommender.model import get_recommendation, retrain_model

app = FastAPI(title="Devasee Book Recommendation System")



@app.get("/")
def root():
    return {"message" : "Welcome To Devasee Book Recommendation System"}



@app.post("/recommend/", response_model=list[BookResponse])
def recommend_books(request: RecommendationRequest):
    print("comming rec req")
    recommendations  = get_recommendation(request.title, request.top_n)

    if not recommendations:
        raise HTTPException(status_code=404, detail=f"No books found for title {request.title}")
    return recommendations

@app.post("/retrain/")
def manual_retrain():
    try:
        retrain_model()
        return {"message": "Manual retraining completed successfully!"}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))






# pip install -U sentence-transformers
