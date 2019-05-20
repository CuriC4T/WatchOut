import time

import picamera



with picamera.PiCamera() as camera:

        camera.start_preview(fullscreen=False, window=(100,20,640,480))
        time.sleep(20)
        camera.stop_preview()
