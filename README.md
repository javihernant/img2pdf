# Img2Pdf
## Description
Simple Spring Boot application that receives an image and converts it into a pdf document.

## Usage
Maven Wrapper has been installed to this project, so there's no need to install maven on your machine.
To run the application you only have to use a simple command on your terminal:

```./mvnw spring-boot:run```

NOTE: Use mvnw.cmd if you're running Windows

### Generate a pdf from an image
Send a POST request to `http://localhost:9080/img2pdf`.
You can use the form at `http://localhost:9080/` to upload your image.

The service will respond with a json containing an id corresponding to the pdf document generated.

### Download your pdf
Use the id provided in order to download your document.

Make a GET request to `http://localhost:9080/pdf/<idDocument>` to obtain your document.
You can directly paste the url on your browser and your download will start immediately