package step.learning.java231web.services.kdf;

/**
 * RFC 2898 Password-Based Cryptography
 * key derivation function (KDF)
 * @author Lector
 */
public interface IKdfService {
    String dk(String password, String salt);
}
