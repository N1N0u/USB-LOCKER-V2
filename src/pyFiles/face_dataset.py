import cv2
import os

def collect_face_data():
    # ===== CAMERA =====
    cam = cv2.VideoCapture("http://10.164.150.182:4747/video")

    if not cam.isOpened():
        print("[ERROR] Cannot open camera", flush=True)
        return

    # ===== LOAD CASCADE =====
    base_dir = os.path.dirname(os.path.abspath(__file__))
    project_src = os.path.dirname(base_dir)
    cascade_path = os.path.join(project_src, "assets", "haarcascade_frontalface_default.xml")
    face_detector = cv2.CascadeClassifier(cascade_path)
	

    if face_detector.empty():
        print("[ERROR] Haar cascade not loaded!", flush=True)
        return

    # ===== INIT =====
    face_id = 1
    count = 0

    print("[INFO] Starting face capture...", flush=True)

    while True:
        ret, img = cam.read()

        if not ret or img is None:
            print("[ERROR] Failed to grab frame", flush=True)
            break

        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

        # ===== DETECTION =====
        faces = face_detector.detectMultiScale(
            gray,
            scaleFactor=1.2,
            minNeighbors=6,
            minSize=(30, 30)
        )

        print(f"[DEBUG] Faces detected: {len(faces)}", flush=True)

        for (x, y, w, h) in faces:
            count += 1

            # Draw rectangle
            cv2.rectangle(img, (x, y), (x + w, y + h), (255, 0, 0), 2)

            # Save face
            cv2.imwrite(
                f"dataset/User.{face_id}.{count}.jpg",
                gray[y:y + h, x:x + w]
            )

            # Show count
            cv2.putText(img, f"{count}/30", (x, y - 10),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.9, (0, 255, 0), 2)

        # ===== DISPLAY =====
        cv2.imshow("Face Capture - ESC to exit", img)

        key = cv2.waitKey(100) & 0xff
        if key == 27 or count >= 30:
            break

    # ===== END =====
    print(f"[INFO] Collected {count} images", flush=True)

    cam.release()
    cv2.destroyAllWindows()


if __name__ == "__main__":
    os.makedirs("dataset", exist_ok=True)
    collect_face_data()