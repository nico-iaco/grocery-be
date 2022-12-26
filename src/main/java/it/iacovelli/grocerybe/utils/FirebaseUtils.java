package it.iacovelli.grocerybe.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import it.iacovelli.grocerybe.exception.UserNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class FirebaseUtils {

    public String verifyTokenAndGetUserid(String token) {
        String userid;
        String filteredToken = token.replace("Bearer ", "");
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(filteredToken);
            userid = decodedToken.getUid();
        } catch (FirebaseAuthException e) {
            throw new UserNotFoundException(e.getMessage());
        }
        return userid;
    }

}
