package it.iacovelli.grocerybe.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import it.iacovelli.grocerybe.exception.UserNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
public class FirebaseUtils {

    private final Log LOGGER = LogFactory.getLog(FirebaseUtils.class);

    public String verifyTokenAndGetUserid(String token) {
        String userid;
        String filteredToken = token.replace("Bearer ", "");
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(filteredToken);
            userid = decodedToken.getUid();
        } catch (FirebaseAuthException e) {
            LOGGER.error("Error while verifying token", e);
            throw new UserNotFoundException("Authentication error");
        }
        return userid;
    }

}
