# Legal Contract basic project

The legal contract basic project demonstrates:

- Connecting a client application to a Fabric blockchain network.
- Submitting smart contract transactions to update ledger state.
- Evaluating smart contract transactions to query ledger state.
- Handling errors in transaction invocation.

## About the project

This project includes smart contract and application code in multiple languages. This project shows create, read,
update,
transfer and delete of an legal contract.

For a more detailed walk-through of the application code and client API usage, refer to
the [Running a Fabric Application tutorial](https://hyperledger-fabric.readthedocs.io/en/latest/write_first_app.html) in
the main Hyperledger Fabric documentation.

### Application

Follow the execution flow in the client application code, and corresponding output on running the application. Pay
attention to the sequence of:

- Transaction invocations (console output like "**--> Submit Transaction**" and "**--> Evaluate Transaction**").
- Results returned by transactions (console output like "**\*\*\* Result**").

### Smart Contract

The smart contract (in folder `chaincode-xyz`) implements the following functions to support the application:

- CreateLegalContract
- ReadLegalContract
- UpdateLegalContract
- DeleteLegalContract

Note that the legal contract implemented by the smart contract is a simplified scenario, without ownership validation,
meant only to demonstrate how to invoke transactions.

## Running the project

The Fabric network is used to deploy and run this project. Follow these steps in order:

1. Create the network and a channel (from the `network` folder).

   ```
   ./network.sh up {CHANNEL_NAME} -c olab -ca
   ```

2. Deploy one of the smart contract implementations (from the `network` folder).

    - To deploy the **Go** chaincode implementation:

      ```shell
      ./network.sh deployCC -ccn {CHAINCODE_NAME} -ccp ../smartcontract/chaincode-go/ -ccl go -c {CHANNEL_NAME}
      ```

3. Run the application (from the `smartcontract` folder).


- To run the **Java** project application:
  ```shell
  cd contract-gateway
  mvn spring-boot:run
  ```

## Clean up

When you are finished, you can bring down the network (from the `network` folder). The command will remove all the nodes
of the network, and delete any ledger data that you created.

```shell
./network.sh down
```
