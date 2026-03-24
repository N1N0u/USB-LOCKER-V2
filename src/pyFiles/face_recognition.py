import cv2
import os


def run_recognition():
    # Load trained model
    recognizer = cv2.face.LBPHFaceRecognizer_create()

    if not os.path.exists('trainer/trainer.yml'):
        print("[ERROR] No trained model found. Run training.py first!")
        return

    recognizer.read('trainer/trainer.yml')

    # Load face detector
    cascade_path = "haarcascade_frontalface_default.xml"
    face_cascade = cv2.CascadeClassifier(cascade_path)

    # Start IP camera
    cam = cv2.VideoCapture("http://10.64.219.73:8080/video")

    # Font for text overlay
    font = cv2.FONT_HERSHEY_SIMPLEX

    # Names mapping (ID -> Name)
    # Add your names here corresponding to IDs used during training
    names = {
        0: "Unknown",
        1: "User1",  # ID 1 from training
        2: "User2",  # ID 2 from training
        # Add more as needed
    }

    print("[INFO] Starting recognition. Press ESC to exit...")

    while True:
        ret, img = cam.read()
        if not ret:
            print("[ERROR] Failed to grab frame")
            break

        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

        # Detect faces
        faces = face_cascade.detectMultiScale(
            gray,
            scaleFactor=1.2,
            minNeighbors=5,
            minSize=(50, 50)  # Minimum face size
        )

        for (x, y, w, h) in faces:
            # Draw rectangle around face
            cv2.rectangle(img, (x, y), (x + w, y + h), (0, 255, 0), 2)

            # Predict identity
            # Returns: (label, confidence)
            # confidence: 0 = perfect match, 100 = very poor match
            id, confidence = recognizer.predict(gray[y:y + h, x:x + w])

            # Lower confidence = better match (LBPH uses distance)
            if confidence < 100:
                name = names.get(id, f"User_{id}")
                confidence_text = f"{round(100 - confidence)}%"
            else:
                name = "unknown"
                confidence_text = "N/A"

            # Display name and confidence
            cv2.putText(img, str(name), (x + 5, y - 5), font, 1, (255, 255, 255), 2)
            cv2.putText(img, confidence_text, (x + 5, y + h - 5), font, 0.5, (255, 255, 0), 1)

        cv2.imshow('Face Recognition - Press ESC to exit', img)

        k = cv2.waitKey(10) & 0xff
        if k == 27:  # ESC
            break

    print("[INFO] Exiting...")
    cam.release()
    cv2.destroyAllWindows()


if __name__ == "__main__":
    run_recognition()