# Use official Python image as base
FROM python:3.10-slim

# Set environment variables
# prevents Python from creating .pyc bytecode files, which reduces clutter.
# ensures Python prints logs/output directly to the console (useful for debugging in Docker).
ENV PYTHONDONTWRITEBYTECODE 1               
ENV PYTHONUNBUFFERED 1

# Sets the working directory inside the container to /app.
WORKDIR /app

# Copy requirements first (for caching)
# Copies requirements.txt from your host machine to the container’s /app directory.
COPY requirements.txt .

# Install dependencies
# Installs Python dependencies listed in requirements.txt.
RUN pip install --no-cache-dir torch==2.8.0
RUN pip install --no-cache-dir -r requirements.txt

# Copy the rest of the project
COPY . .

# Expose FastAPI port
EXPOSE 8000

# Command to run FastAPI via Uvicorn
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]
