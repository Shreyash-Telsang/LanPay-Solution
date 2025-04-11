from connection import mysql

class crud:
    def __init__(self):
        self.conn = mysql.connect()
        self.cursor = self.conn.cursor()

    def get_all_profiles(self):
        self.cursor.execute("SELECT * FROM profile")
        return self.cursor.fetchall()

    def get_profile_by_condition(self, column, value):
        VALID_COLUMNS = {"Account", "Private_Key", "Balance", "Name", "Phoneno", "Ration_Card", "Mail", "PIN"}
        if column not in VALID_COLUMNS:
            return None

        sql = f"SELECT * FROM profile WHERE {column} = %s"
        self.cursor.execute(sql, (value,))
        row = self.cursor.fetchone()  # Only get the first match

        if row is None:
            return None

        # Get column names
        columns = [desc[0] for desc in self.cursor.description]
        return dict(zip(columns, row))

    def insert_profile(self, name, email, age):
        sql = "INSERT INTO profile (name, email, age) VALUES (%s, %s, %s)"
        values = (name, email, age)
        self.cursor.execute(sql, values)
        self.conn.commit()
        return {"message": "Profile added successfully!"}

    def update_balance(self, Balance, Account):
        sql = "UPDATE profile SET Balance=%s WHERE Account=%s"  # Fixed typo: "Balane" → "Balance"
        values = (Balance, Account)
        self.cursor.execute(sql, values)
        self.conn.commit()
        return {"message": "Profile updated successfully!"}

    def delete_profile(self, profile_id):
        sql = "DELETE FROM profile WHERE id=%s"
        self.cursor.execute(sql, (profile_id,))
        self.conn.commit()
        return {"message": "Profile deleted successfully!"}
