from flask import Flask, request, jsonify
import crud

app = Flask(__name__)

@app.route("/profiles", methods=["GET"])
def get_profiles():
    ObjectData = crud.crud()
    data = ObjectData.get_all_profiles()
    return jsonify(data)

@app.route("/profile", methods=["GET"])
def get_profile():
    column = request.args.get("column")
    value = request.args.get("value")

    if not column or not value:
        return jsonify({"error": "Missing column or value"}), 400

    ObjectData = crud.crud()
    data = ObjectData.get_profile_by_condition(column, value)
    return jsonify(data)

@app.route("/insert", methods=["POST"])
def insert_profile():
    data = request.get_json()
    name = data.get("name")
    email = data.get("email")
    age = data.get("age")

    if not name or not email or not age:
        return jsonify({"error": "Missing required fields"}), 400

    ObjectData = crud.crud()
    response = ObjectData.insert_profile(name, email, age)
    return jsonify(response)

@app.route("/update/<int:profile_id>", methods=["PUT"])
def update_profile(profile_id):
    data = request.get_json()
    name = data.get("name")
    email = data.get("email")
    age = data.get("age")

    if not name or not email or not age:
        return jsonify({"error": "Missing required fields"}), 400

    ObjectData = crud.crud()
    response = ObjectData.update_profile(profile_id, name, email, age)
    return jsonify(response)

@app.route("/delete/<int:profile_id>", methods=["DELETE"])
def delete_profile(profile_id):
    ObjectData = crud.crud()
    response = ObjectData.delete_profile(profile_id)
    return jsonify(response)

if __name__ == "__main__":
    app.run(host="0.0.0.0", debug=True)
