import cv2


def collect_face_data():
    # Use phone IP camera instead of webcam
    # Replace with your phone's IP webcam app address
    cam = cv2.VideoCapture("http://10.64.219.73:8080/video")

    # Load Haar Cascade for face detection
    face_detector = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')

    # Get user ID from input
    face_id = input('\nEnter user ID (number): ')

    print("\n[INFO] Initializing face capture. Look at the camera...")
    count = 0

    while True:
        ret, img = cam.read()
        if not ret:
            print("[ERROR] Failed to grab frame")
            break

        # Convert to grayscale for detection
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

        # Detect faces
        faces = face_detector.detectMultiScale(
            gray,
            scaleFactor=1.3,  # How much image size is reduced at each scale
            minNeighbors=5  # How many neighbors each candidate rectangle should have
        )

        for (x, y, w, h) in faces:
            # Draw rectangle around face
            cv2.rectangle(img, (x, y), (x + w, y + h), (255, 0, 0), 2)
            count += 1

            # Save the captured face (grayscale, cropped to face only)
            cv2.imwrite(f"dataset/User.{face_id}.{count}.jpg",
                        gray[y:y + h, x:x + w])

            # Display count on image
            cv2.putText(img, f"Count: {count}/30", (x, y - 10),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.9, (0, 255, 0), 2)

        cv2.imshow('Collecting Face Data - Press ESC to exit', img)

        # Wait for 100ms, check for ESC key (27)
        k = cv2.waitKey(100) & 0xff
        if k == 27:  # ESC pressed
            break
        elif count >= 30:  # Take 30 samples
            break

    print(f"\n[INFO] Collected {count} images. Exiting...")
    cam.release()
    cv2.destroyAllWindows()


if __name__ == "__main__":
    # Create dataset folder if it doesn't exist
    import os

    os.makedirs("dataset", exist_ok=True)
    collect_face_data()