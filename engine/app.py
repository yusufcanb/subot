import random
import json

import torch

from model import NeuralNet
from nltk_utils import bag_of_words, tokenize

from flask import Flask, request, jsonify

device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

with open('subot.json', 'r') as json_data:
    intents = json.load(json_data)

FILE = "data.pth"
ACC_THRESHOLD = 0.92

data = torch.load(FILE)

input_size = data["input_size"]
hidden_size = data["hidden_size"]
output_size = data["output_size"]
all_words = data['all_words']
tags = data['tags']
model_state = data["model_state"]

model = NeuralNet(input_size, hidden_size, output_size).to(device)
model.load_state_dict(model_state)
model.eval()

bot_name = "Yiğit Bot"

app = Flask(__name__)

@app.route("/")
def chatbot():
    sentence = request.args.get("question", None)

    if not sentence:
        return "Bad Request"

    sentence = tokenize(sentence)
    X = bag_of_words(sentence, all_words)
    X = X.reshape(1, X.shape[0])
    X = torch.from_numpy(X).to(device)

    output = model(X)
    _, predicted = torch.max(output, dim=1)

    tag = tags[predicted.item()]

    probs = torch.softmax(output, dim=1)
    prob = probs[0][predicted.item()]
    print(prob.item())
    if prob.item() > ACC_THRESHOLD:
        for intent in intents['intents']:
            if tag == intent["tag"]:
                return f"{random.choice(intent['responses'])}"
    else:
        return "I do not understand. I'm continue to improve myself day by day. You can visit for further information. https://sabanciuniv.edu/en/faq"

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=False)