from flask import Flask, request, jsonify
import crud

data_sender=None
data_receiver=None
amount=None

app = Flask(__name__)

@app.route('/send', methods=['POST'])
def send_text():
    text = request.form.get('text')
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
    

    ObjectData = crud.crud()
    data_sender=None
    data_receiver=None
    print("AAyi")

    receiver = request.form.get('to')
    

    data_receiver = ObjectData.get_profile_by_condition("Account", str(receiver))
   

    if not data_receiver:
        return jsonify({"error": "Check qr"})


    sender= request.form.get('from')
    data_sender = ObjectData.get_profile_by_condition("Account", str(sender))
    if not data_sender:
        return jsonify({"error": "Check receiver"})
    print(receiver)
    return jsonify({"Success": "Account found details confirmed"})
    

@app.route('/amount', methods=['POST'])
def amount():
    # amount=None
    # amount = request.form.get('Amount')
    data = request.get_json()
    amount = float(data.get('Amount'))
    if not amount:
        return jsonify({"error": amount})
    else:
        balance= data_sender['Balance']
        if balance < amount:
            return jsonify({"error": "Insufficient balance"})
        else:
            return jsonify({"Success": "Amount confirmed"})

@app.route('/pin', methods=['POST'])
def pin():
    pin = request.form.get('pin')
    if not pin:
        return jsonify({"error": "Check pin"})
    if pin == data_sender['PIN']:
        return jsonify({"Success": "Pin confirmed"})
    else:
        return jsonify({"error": "Incorrect pin"}) 


@app.route('/transaction', methods=['POST'])
def transaction():
    # Transaction function here
    # if transaction successful:
    ObjectData = crud.crud()
    response1 = ObjectData.update_balance(data_sender['Balance'] - amount, data_sender['Account'])
    response2 = ObjectData.update_balance(data_receiver['Balance'] + amount, data_receiver['Account'])
    return jsonify({"Success": "Transaction successful!"})



if __name__ == '__main__':
    app.run(host='0.0.0.0',debug=True)