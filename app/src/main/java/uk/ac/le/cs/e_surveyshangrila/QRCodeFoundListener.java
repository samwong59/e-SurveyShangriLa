package uk.ac.le.cs.e_surveyshangrila;

public interface QRCodeFoundListener {

    void onQrCodeFound(String qrCode);
    void qrCodeNotFound();

}
