FROM pytorch/pytorch:1.9.0-cuda10.2-cudnn7-runtime

WORKDIR /app

COPY . /app/

RUN pip install -r requirements.txt
RUN python -c "import nltk;nltk.download('punkt')"

CMD ["python", "/app/app.py"]

EXPOSE 5000