package com.abdul.legalcontract.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.TlsChannelCredentials;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.Gateway;
import org.hyperledger.fabric.client.Hash;
import org.hyperledger.fabric.client.Network;
import org.hyperledger.fabric.client.identity.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

@Configuration
public class FabricConfig {
    public static final String MSP_ID = System.getenv().getOrDefault("MSP_ID", "Org1MSP");
    // Gateway peer end point.
    public static final String PEER_ENDPOINT = "localhost:7051";
    public static final String OVERRIDE_AUTH = "peer0.org1.example.com";
    // Path to crypto materials.
    public static final Path CRYPTO_PATH =
            Paths.get("").toAbsolutePath()
                    .resolve("network/organizations/peerOrganizations/org1.example.com");
    // Path to user certificate.
    public static final Path CERT_DIR_PATH =
            CRYPTO_PATH.resolve(Paths.get("users/User1@org1.example.com/msp/signcerts"));
    // Path to user private key directory.
    public static final Path KEY_DIR_PATH =
            CRYPTO_PATH.resolve(Paths.get("users/User1@org1.example.com/msp/keystore"));
    // Path to peer tls certificate.
    public static final Path TLS_CERT_PATH =

            CRYPTO_PATH.resolve(Paths.get("peers/peer0.org1.example.com/tls/ca.crt"));

    public static final String CHANNEL_NAME =
            System.getenv().getOrDefault("CHANNEL_NAME", "olab");
    public static final String CHAINCODE_NAME =
            System.getenv().getOrDefault("CHAINCODE_NAME", "legalContract");

    @Bean
    public Gson getGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    @Bean
    public Contract getLegalContractBean(Network network) {
        // Use the injected Gateway bean to get the contract
        return network.getContract(CHAINCODE_NAME);
    }

    @Bean
    public Network getOlabNetwork(Gateway gateway) {
        // Use the injected Gateway bean to get the contract
        return gateway.getNetwork(CHANNEL_NAME);
    }

    @Bean
    public Gateway getGateway(ManagedChannel managedChannel)
            throws CertificateException, IOException, InvalidKeyException {
        Gateway.Builder builder = Gateway.newInstance()
                .identity(newIdentity())
                .signer(newSigner())
                .hash(Hash.SHA256)
                .connection(managedChannel)
                // Default timeouts for different gRPC calls
                .evaluateOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .endorseOptions(options -> options.withDeadlineAfter(15, TimeUnit.SECONDS))
                .submitOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .commitStatusOptions(options -> options.withDeadlineAfter(1, TimeUnit.MINUTES));
        return builder.connect();
    }

    @Bean
    public ManagedChannel newGrpcConnection() throws IOException {
        var credentials = TlsChannelCredentials.newBuilder()
                .trustManager(TLS_CERT_PATH.toFile())
                .build();
        return Grpc.newChannelBuilder(PEER_ENDPOINT, credentials)
                .overrideAuthority(OVERRIDE_AUTH)
                .build();
    }

    public Identity newIdentity() throws IOException, CertificateException {
        try (var certReader = Files.newBufferedReader(getFirstFilePath(CERT_DIR_PATH))) {
            var certificate = Identities.readX509Certificate(certReader);
            return new X509Identity(MSP_ID, certificate);
        }
    }

    public Signer newSigner() throws IOException, InvalidKeyException {
        try (var keyReader = Files.newBufferedReader(getFirstFilePath(KEY_DIR_PATH))) {
            var privateKey = Identities.readPrivateKey(keyReader);
            return Signers.newPrivateKeySigner(privateKey);
        }
    }

    public Path getFirstFilePath(Path dirPath) throws IOException {
        try (var keyFiles = Files.list(dirPath)) {
            return keyFiles.findFirst().orElseThrow();
        }
    }
}
