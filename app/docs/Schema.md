/users
  /{userId}
    - name: "John Doe"
    - email: "john@example.com"
    - createdAt: Timestamp
    - updatedAt: Timestamp

/medicines
  /{medicineId}
    - name: "Aspirin"
    - dosage: 55
    - image: "https://example.com/aspirin.jpg"
    - description: "Pain reliever"
    - userId: "{userId}"
    - createdAt: Timestamp
    - updatedAt: Timestamp

/dosageRecords
  /{recordId}
    - medicineId: "{medicineId}"
    - userId: "{userId}"
    - dosageTaken: "500mg"
    - timeTaken: Timestamp
    - notes: "Took after breakfast"
    - createdAt: Timestamp

/reminders
  /{reminderId}
    - userId: "{userId}"
    - medicineId: "{medicineId}"
    - reminderTime: Timestamp
    - frequency: "daily"
    - createdAt: Timestamp
