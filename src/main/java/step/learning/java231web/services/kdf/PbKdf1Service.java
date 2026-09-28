package step.learning.java231web.services.kdf;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.java231web.services.hash.IHashService;

/**
 * RFC 2898 Password-Based Cryptography
 * Sec 5.1 PBKDF1
 * @author Lector
 */
@Singleton
public class PbKdf1Service implements IKdfService {
    private final int iterationCount = 1000000;
    private final IHashService hashService;

    @Inject
    public PbKdf1Service(IHashService hashService) {
        this.hashService = hashService;
    }
    
    
    @Override
    public String dk(String password, String salt) {
        String t = password + salt;
        for(int i = 0; i < iterationCount; i++) {
            t = hashService.hexDigest(t);
        }
        return t;
    }
    
}
