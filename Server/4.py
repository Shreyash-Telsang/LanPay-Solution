from flask import Flask, request, jsonify
import crud

data_sender = None
data_receiver = None
amount = None

app = Flask(__name__)

@app.route('/send', methods=['POST'])
def send_text():
    data = request.get_json()
    text = data.get('text')
    print(text)
    if text:
        return jsonify({"message": f"Received text: {text}"})
    else:
        return jsonify({"error": "No text provided"})


@app.route('/get', methods=['GET'])
def get_text():
    sample_text = "Hello"
    return jsonify({"message": sample_text})


@app.route('/qr', methods=['POST'])
def start():
    global data_sender, data_receiver

    ObjectData = crud.crud()
    data = request.get_json()

    print("QR route hit")

    receiver = data.get('to')
    sender = data.get('from')

    if not receiver or not sender:
        return jsonify({"error": "Missing sender or receiver account"})

    data_receiver = ObjectData.get_profile_by_condition("Account", str(receiver))
    if not data_receiver:
        return jsonify({"error": "Invalid receiver QR"})

    data_sender = ObjectData.get_profile_by_condition("Account", str(sender))
    print(data_sender)
    if not data_sender:
        return jsonify({"error": "Invalid sender account"})

    print("Receiver:", receiver)
    return jsonify({"Success": "Account found, details confirmed"})


@app.route('/amount', methods=['POST'])
def amount_route():
    global amount

    data = request.get_json()
    if not data or 'Amount' not in data:
        return jsonify({"error": "Missing amount"})

    try:
        amount = float(data.get('Amount'))
    except ValueError:
        return jsonify({"error": "Invalid amount format"})

    if data_sender is None:
        return jsonify({"error": "Sender details not found"})

    balance = data_sender.get('Balance')
    print(balance)
    if balance < amount:
        return jsonify({"error": "Insufficient balance"})
    else:
        return jsonify({"Success": "Amount confirmed"})


@app.route('/pin', methods=['POST'])
def pin():
    data = request.get_json()
    pin = data.get('pin')

    if not pin:
        return jsonify({"error": "Missing pin"})

    if data_sender is None:
        return jsonify({"error": "Sender data not loaded"})

    if str(pin) == str(data_sender['PIN']):
        return jsonify({"Success": "Pin confirmed"})
    else:
        return jsonify({"error": "Incorrect pin"})


@app.route('/transaction', methods=['POST'])
def transaction():
    
    if not all([data_sender, data_receiver, amount]):
        return jsonify({"error": "Incomplete transaction context"})

    ObjectData = crud.crud()

    new_sender_balance = data_sender['Balance'] - amount
    new_receiver_balance = data_receiver['Balance'] + amount

    response1 = ObjectData.update_balance(new_sender_balance, data_sender['Account'])
    response2 = ObjectData.update_balance(new_receiver_balance, data_receiver['Account'])

    return jsonify({"Success": "Transaction successful!"})


if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)
