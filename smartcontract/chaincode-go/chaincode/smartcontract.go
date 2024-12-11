package chaincode

import (
	"encoding/json"
	"fmt"

	"github.com/hyperledger/fabric-contract-api-go/v2/contractapi"
)

// SmartContract provides functions for managing an LegalContract
type SmartContract struct {
	contractapi.Contract
}

// LegalContract describes the structure for the legalContract with participants, description, and title
type LegalContract struct {
	ID           string   `json:"ID"`
	Title        string   `json:"Title"`
	Description  string   `json:"Description"`
	Participants []string `json:"Participants"`
}

// InitLedger initializes the ledger with some sample legalContracts
func (s *SmartContract) InitLedger(ctx contractapi.TransactionContextInterface) error {
	legalContracts := []LegalContract{
		{
			ID:           "asset1",
			Title:        "Project Alpha",
			Description:  "Initial project setup",
			Participants: []string{"Alice", "Bob"},
		},
		{
			ID:           "asset2",
			Title:        "Project Beta",
			Description:  "Development phase",
			Participants: []string{"Charlie", "Diana"},
		},
	}

	for _, legalContract := range legalContracts {
		legalContractJSON, err := json.Marshal(legalContract)
		if err != nil {
			return err
		}

		err = ctx.GetStub().PutState(legalContract.ID, legalContractJSON)
		if err != nil {
			return fmt.Errorf("failed to put to world state. %v", err)
		}
	}

	return nil
}

// CreateLegalContract creates a new legalContract in the ledger
func (s *SmartContract) CreateLegalContract(
	ctx contractapi.TransactionContextInterface,
	id string,
	title string,
	description string,
	participantsJSON string) error {
	// Parse the JSON string into a Go slice
	var participants []string
	err := json.Unmarshal([]byte(participantsJSON), &participants)
	if err != nil {
		return fmt.Errorf("error parsing participants parameter: %v", err)
	}

	// Create the legalContract
	legalContract := LegalContract{
		ID:           id,
		Title:        title,
		Description:  description,
		Participants: participants,
	}

	legalContractJSON, err := json.Marshal(legalContract)
	if err != nil {
		return fmt.Errorf("error marshaling legalContract: %v", err)
	}

	return ctx.GetStub().PutState(id, legalContractJSON)
}

// ReadLegalContract retrieves an legalContract from the ledger by ID
func (s *SmartContract) ReadLegalContract(
	ctx contractapi.TransactionContextInterface,
	id string) (*LegalContract, error) {
	legalContractJSON, err := ctx.GetStub().GetState(id)
	if err != nil {
		return nil, fmt.Errorf("failed to read from world state: %v", err)
	}
	if legalContractJSON == nil {
		return nil, fmt.Errorf("the legalContract %s does not exist", id)
	}

	var legalContract LegalContract
	err = json.Unmarshal(legalContractJSON, &legalContract)
	if err != nil {
		return nil, err
	}

	return &legalContract, nil
}

// UpdateLegalContract updates an existing legalContract with new details
func (s *SmartContract) UpdateLegalContract(
	ctx contractapi.TransactionContextInterface,
	id string,
	title string,
	description string,
	participantsJSON string) error {
	exists, err := s.LegalContractExists(ctx, id)
	if err != nil {
		return err
	}
	if !exists {
		return fmt.Errorf("the legalContract %s does not exist", id)
	}

	// Parse the JSON string into a Go slice
	var participants []string
	err = json.Unmarshal([]byte(participantsJSON), &participants)
	if err != nil {
		return fmt.Errorf("error parsing participants parameter: %v", err)
	}

	// Overwriting original legalContract with new details
	legalContract := LegalContract{
		ID:           id,
		Title:        title,
		Description:  description,
		Participants: participants,
	}
	legalContractJSON, err := json.Marshal(legalContract)
	if err != nil {
		return fmt.Errorf("error marshaling legalContract: %v", err)
	}

	return ctx.GetStub().PutState(id, legalContractJSON)
}

// DeleteLegalContract removes an legalContract from the ledger
func (s *SmartContract) DeleteLegalContract(ctx contractapi.TransactionContextInterface, id string) error {
	exists, err := s.LegalContractExists(ctx, id)
	if err != nil {
		return err
	}
	if !exists {
		return fmt.Errorf("the legalContract %s does not exist", id)
	}

	return ctx.GetStub().DelState(id)
}

// LegalContractExists checks if an legalContract exists in the ledger
func (s *SmartContract) LegalContractExists(ctx contractapi.TransactionContextInterface, id string) (bool, error) {
	legalContractJSON, err := ctx.GetStub().GetState(id)
	if err != nil {
		return false, fmt.Errorf("failed to read from world state: %v", err)
	}

	return legalContractJSON != nil, nil
}

// GetAllLegalContracts retrieves all legalContracts from the ledger
func (s *SmartContract) GetAllLegalContracts(ctx contractapi.TransactionContextInterface) ([]*LegalContract, error) {
	resultsIterator, err := ctx.GetStub().GetStateByRange("", "")
	if err != nil {
		return nil, err
	}
	defer resultsIterator.Close()

	var legalContracts []*LegalContract
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return nil, err
		}

		var legalContract LegalContract
		err = json.Unmarshal(queryResponse.Value, &legalContract)
		if err != nil {
			return nil, err
		}
		legalContracts = append(legalContracts, &legalContract)
	}

	return legalContracts, nil
}
