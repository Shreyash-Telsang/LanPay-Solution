from flask import Flask, request, jsonify
from flask_cors import CORS
import mysql.connector

app = Flask(__name__)
CORS(app)

# Database configuration
db_config = {
    "host": "localhost",
    "user": "root",
    "password": "",
    "database": "hackathon"
}

def get_db_connection():
    return mysql.connector.connect(**db_config)

@app.route("/Python/get_profile", methods=["POST"])
def get_profile():
    try:
        data = request.get_json()
        account = data.get("account")

        if not account:
            return jsonify({"error": "Account parameter is required"}), 400

        db = get_db_connection()
        cursor = db.cursor(dictionary=True)

        cursor.execute("SELECT * FROM profile WHERE Account = %s", (account,))
        result = cursor.fetchone()

        cursor.close()
        db.close()

        if result:
            profile_data = {
                "Account": result["Account"],
                "Private Key": result["Private Key"],
                "Balance": float(result["Balance"]),
                "Name": result["Name"],
                "Phone no.": result["Phone no."],
                "Ration card no.": result["Ration card no."],
                "Mail id": result["Mail id"],
                "PIN": int(result["PIN"])
            }
            return jsonify(profile_data)
        else:
            return jsonify({"error": f"Account {account} not found"}), 404

    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route("/Python/update_profile", methods=["POST"])
def update_profile():
    try:
        data = request.get_json()

        required_fields = ["Account", "Name", "Phone no.", "Ration card no.", "Mail id", "PIN"]
        if not all(field in data for field in required_fields):
            return jsonify({"error": "Missing required fields"}), 400

        db = get_db_connection()
        cursor = db.cursor(dictionary=True)

        cursor.execute("""
            UPDATE profile SET 
                Name = %s,
                `Phone no.` = %s,
                `Ration card no.` = %s,
                `Mail id` = %s,
                PIN = %s
            WHERE Account = %s
        """, (
            data["Name"],
            data["Phone no."],
            data["Ration card no."],
            data["Mail id"],
            int(data["PIN"]),
            data["Account"]
        ))

        db.commit()

        cursor.execute("SELECT * FROM profile WHERE Account = %s", (data["Account"],))
        updated_profile = cursor.fetchone()

        cursor.close()
        db.close()

        if updated_profile:
            return jsonify({
                "Account": updated_profile["Account"],
                "Private Key": updated_profile["Private Key"],
                "Balance": float(updated_profile["Balance"]),
                "Name": updated_profile["Name"],
                "Phone no.": updated_profile["Phone no."],
                "Ration card no.": updated_profile["Ration card no."],
                "Mail id": updated_profile["Mail id"],
                "PIN": int(updated_profile["PIN"])
            })
        else:
            return jsonify({"error": "Profile not found after update"}), 404

    except Exception as e:
        if 'db' in locals():
            db.rollback()
            db.close()
        return jsonify({"error": str(e)}), 500

@app.route("/Python/delete_account", methods=["POST"])
def delete_account():
    try:
        data = request.get_json()
        account = data.get("account")

        if not account:
            return jsonify({"error": "Account parameter is required"}), 400

        db = get_db_connection()
        cursor = db.cursor()

        cursor.execute("DELETE FROM profile WHERE Account = %s", (account,))
        db.commit()

        if cursor.rowcount > 0:
            cursor.close()
            db.close()
            return jsonify({"success": True}), 200
        else:
            cursor.close()
            db.close()
            return jsonify({"error": "Account not found"}), 404

    except Exception as e:
        if 'db' in locals():
            db.rollback()
            db.close()
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000, debug=True)