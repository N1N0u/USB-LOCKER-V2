import cv2
import os


def run_recognition():
    recognizer = cv2.face.LBPHFaceRecognizer_create()

    if not os.path.exists('trainer/trainer.yml'):
        print("[ERROR] No trained model found!", flush=True)
        return

    recognizer.read('trainer/trainer.yml')

   
    face_cascade = cv2.CascadeClassifier(
        cv2.data.haarcascades + "haarcascade_frontalface_default.xml"
    )

    cam = cv2.VideoCapture("http://10.164.150.182:4747/video")

    font = cv2.FONT_HERSHEY_SIMPLEX

    names = {
        1: "User1"
    }

    print("[INFO] Recognition started", flush=True)

    recognized = False

    while True:
        ret, img = cam.read()
        if not ret:
            print("[ERROR] Camera failed", flush=True)
            break

        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

        faces = face_cascade.detectMultiScale(gray, 1.2, 5, minSize=(50, 50))

        for (x, y, w, h) in faces:
            id, confidence = recognizer.predict(gray[y:y + h, x:x + w])

            
            if confidence < 60:
                name = names.get(id, "User")
                confidence_text = f"{round(100 - confidence)}%"

                #  SIGNAL TO JAVA
                print("AUTHORIZED", flush=True)

                recognized = True

            else:
                name = "Unknown"
                confidence_text = "N/A"

            cv2.rectangle(img, (x, y), (x + w, y + h), (0, 255, 0), 2)
            cv2.putText(img, name, (x, y - 5), font, 1, (255, 255, 255), 2)
            cv2.putText(img, confidence_text, (x, y + h - 5), font, 0.5, (255, 255, 0), 1)

        cv2.imshow("Recognition", img)

        if recognized:
            break

        if cv2.waitKey(10) & 0xff == 27:
            break

    cam.release()
    cv2.destroyAllWindows()

    print("[INFO] End", flush=True)


if __name__ == "__main__":
    run_recognition()