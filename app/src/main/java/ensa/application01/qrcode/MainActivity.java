package ensa.application01.qrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.WriterException;

import java.util.UUID;
import DataBase.UsersDataSource;
import Entity.User;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private UsersDataSource datasource;


    private ImageView qrCodeIV;
    private EditText dataEmail;
    private EditText dataPasswd;
    private Button generateQrBtn;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = new UsersDataSource(this);
        try {
            datasource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        qrCodeIV = findViewById(R.id.idIVQrcode);
        dataEmail = findViewById(R.id.idEmail);
        dataPasswd = findViewById(R.id.idPasswd);
        generateQrBtn = findViewById(R.id.idBtnGenerateQR);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openNewActivity();
        }
    });
    }


    public void openNewActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void onClick(View v) {
        String Email = dataEmail.getText().toString();
        String Passwd = dataEmail.getText().toString();
        if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(Passwd) ) {

            // if the edittext inputs are empty then execute
            // this method showing a toast message.
            Toast.makeText(MainActivity.this, "Enter some text to generate QR Code", Toast.LENGTH_SHORT).show();
        } else if(Email.contains(";") || Passwd.contains(";")) {
            Toast.makeText(MainActivity.this, "Email or Passwd Non Valide", Toast.LENGTH_SHORT).show();
        }else{
            // below line is for getting
            // the windowmanager service.
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

            // initializing a variable for default display.
            Display display = manager.getDefaultDisplay();

            // creating a variable for point which
            // is to be displayed in QR Code.
            Point point = new Point();
            display.getSize(point);

            // getting width and
            // height of a point
            int width = point.x;
            int height = point.y;

            // generating dimension from width and height.
            int dimen = width > height ? width : height;
            dimen = dimen * 3 / 4;

            // setting this dimensions inside our qr code
            // encoder to generate our qr code.
//            String uniqueID = UUID.randomUUID().toString();
            String QrCodeString = Email + ";" + Passwd;
            User user = new User();
//            user.setId(uniqueID);
            user.setEmail(Email);
            user.setPasswd(Passwd);
            datasource.createUser(user);
            qrgEncoder = new QRGEncoder(QrCodeString, null, QRGContents.Type.TEXT, dimen);
            try {
                // getting our qrcode in the form of bitmap.
                bitmap = qrgEncoder.encodeAsBitmap();
                // the bitmap is set inside our image
                // view using .setimagebitmap method.
                qrCodeIV.setImageBitmap(bitmap);
            } catch (WriterException e) {
                // this method is called for
                // exception handling.
                Log.e("Tag", e.toString());
            }
        }
    }
}