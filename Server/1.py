from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/send', methods=['POST'])
def send_text():
    text = request.form.get('text')
    print(text)
    if text:
        return jsonify({"message": f"Received text: {text}"}), 200
    else:
        return jsonify({"error": "No text provided"}), 400

@app.route('/get', methods=['GET'])
def get_text():
    sample_text = "Hello"
    return jsonify({"message": sample_text})

if __name__ == '__main__':
    app.run(debug=True)