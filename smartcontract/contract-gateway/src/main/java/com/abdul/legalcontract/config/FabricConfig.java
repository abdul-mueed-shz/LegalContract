package com.abdul.legalcontract.config;

import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.TlsChannelCredentials;
import lombok.RequiredArgsConstructor;
import org.hyperledger.fabric.client.*;
import org.hyperledger.fabric.client.identity.*;
import org.springframework.beans.factory.annotation.Value;
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
@RequiredArgsConstructor
public class FabricConfig {

    @Value("${fabric.msp-id}")
    public String mspId;

    // Gateway peer end point.
    @Value("${fabric.peer-endpoint}")
    private String peerEndpoint;

    @Value("${fabric.override-auth}")
    public String overrideAuth;

    @Value("${fabric.network-organizations-path}")
    public String peerOrganizationPath;

    @Value("${fabric.peer-organization}")
    public String peerOrganizationName;

    @Value("${fabric.peer-organization-user-path}")
    public String peerOrganizationUserPath;

    @Value("${fabric.peer-organization-tls-cert-path}")
    public String peerOrganizationTlsCertPath;

    @Value("${fabric.channel-name}")
    public String channelName;

    @Value("${fabric.chaincode-name}")
    public String chaincodeName;

    @Bean
    Checkpointer getFileCheckPointer() {
        return new InMemoryCheckpointer();
    }

    @Bean("legalContract")
    public Contract getLegalContractBean(Network network) {
        // Use the injected Gateway bean to get the contract
        return network.getContract(chaincodeName);
    }

    @Bean("olab")
    public Network getOlabNetwork(Gateway gateway) {
        // Use the injected Gateway bean to get the contract
        return gateway.getNetwork(channelName);
    }

    @Bean
    public Gateway getGateway(ManagedChannel managedChannel)
            throws CertificateException, IOException, InvalidKeyException {

        Path cryptoPath = getNetworkOrganizationsBasePath();
        Path certDirectoryPath = cryptoPath.resolve(Paths.get(peerOrganizationUserPath + "/msp/signcerts"));
        Path keyDirectoryPath = cryptoPath.resolve(Paths.get(peerOrganizationUserPath + "/msp/keystore"));

        Gateway.Builder builder = Gateway.newInstance()
                .identity(newIdentity(certDirectoryPath))
                .signer(newSigner(keyDirectoryPath))
                .hash(Hash.SHA256)
                .connection(managedChannel)
                .evaluateOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .endorseOptions(options -> options.withDeadlineAfter(15, TimeUnit.SECONDS))
                .submitOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .commitStatusOptions(options -> options.withDeadlineAfter(1, TimeUnit.MINUTES));
        return builder.connect();
    }

    @Bean
    public ManagedChannel newGrpcConnection() throws IOException {
        Path tlsCertificatePath = getNetworkOrganizationsBasePath().resolve(Paths.get(peerOrganizationTlsCertPath));
        var credentials = TlsChannelCredentials.newBuilder()
                .trustManager(tlsCertificatePath.toFile())
                .build();
        return Grpc.newChannelBuilder(peerEndpoint, credentials)
                .overrideAuthority(overrideAuth)
                .build();
    }

    public Identity newIdentity(Path certDirectoryPath) throws IOException, CertificateException {
        try (var certReader = Files.newBufferedReader(getFirstFilePath(certDirectoryPath))) {
            var certificate = Identities.readX509Certificate(certReader);
            return new X509Identity(mspId, certificate);
        }
    }

    public Signer newSigner(Path keyDirectoryPath) throws IOException, InvalidKeyException {
        try (var keyReader = Files.newBufferedReader(getFirstFilePath(keyDirectoryPath))) {
            var privateKey = Identities.readPrivateKey(keyReader);
            return Signers.newPrivateKeySigner(privateKey);
        }
    }

    public Path getFirstFilePath(Path dirPath) throws IOException {
        try (var keyFiles = Files.list(dirPath)) {
            return keyFiles.findFirst().orElseThrow();
        }
    }

    public Path getNetworkOrganizationsBasePath() {
        return Paths.get("").toAbsolutePath().resolve(peerOrganizationPath + peerOrganizationName);
    }


}
