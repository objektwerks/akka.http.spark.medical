package medical

import java.io.FileInputStream
import java.security.{KeyStore, SecureRandom}

import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}

case class SSLContextConf(passphrase: String,
                          keystorePath: String,
                          keystoreType: String,
                          sslProtocol: String,
                          algorithm: String)

object SSLContextFactory {
  def newInstance(conf: SSLContextConf): SSLContext = {
    val inputstream = new FileInputStream(conf.keystorePath)
    val password = conf.passphrase.toCharArray
    val keystore = KeyStore.getInstance(conf.keystoreType)
    keystore.load(inputstream, password)
    require(keystore != null, "Keystore is null. Load a valid keystore file.")

    val keyManagerFactory = KeyManagerFactory.getInstance(conf.algorithm)
    keyManagerFactory.init(keystore, password)

    val trustManagerFactory = TrustManagerFactory.getInstance(conf.algorithm)
    trustManagerFactory.init(keystore)

    val sslContext = SSLContext.getInstance(conf.sslProtocol)
    sslContext.init(keyManagerFactory.getKeyManagers, trustManagerFactory.getTrustManagers, new SecureRandom)
    sslContext
  }
}