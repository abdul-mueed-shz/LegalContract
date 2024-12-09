package chaincode

import (
	"encoding/json"
	"fmt"

	"github.com/hyperledger/fabric-contract-api-go/v2/contractapi"
)

// SmartContract provides functions for managing an Asset
type SmartContract struct {
	contractapi.Contract
}

// Asset describes the structure for the asset with participants, description, and title
type Asset struct {
	ID           string   `json:"ID"`
	Title        string   `json:"Title"`
	Description  string   `json:"Description"`
	Participants []string `json:"Participants"`
}

// InitLedger initializes the ledger with some sample assets
func (s *SmartContract) InitLedger(ctx contractapi.TransactionContextInterface) error {
	assets := []Asset{
		{ID: "asset1", Title: "Project Alpha", Description: "Initial project setup", Participants: []string{"Alice", "Bob"}},
		{ID: "asset2", Title: "Project Beta", Description: "Development phase", Participants: []string{"Charlie", "Diana"}},
	}

	for _, asset := range assets {
		assetJSON, err := json.Marshal(asset)
		if err != nil {
			return err
		}

		err = ctx.GetStub().PutState(asset.ID, assetJSON)
		if err != nil {
			return fmt.Errorf("failed to put to world state. %v", err)
		}
	}

	return nil
}

// CreateAsset creates a new asset in the ledger
func (s *SmartContract) CreateAsset(ctx contractapi.TransactionContextInterface, id string, title string, description string, participantsJSON string) error {
	// Parse the JSON string into a Go slice
	var participants []string
	err := json.Unmarshal([]byte(participantsJSON), &participants)
	if err != nil {
		return fmt.Errorf("error parsing participants parameter: %v", err)
	}

	// Create the asset
	asset := Asset{
		ID:           id,
		Title:        title,
		Description:  description,
		Participants: participants,
	}

	assetJSON, err := json.Marshal(asset)
	if err != nil {
		return fmt.Errorf("error marshaling asset: %v", err)
	}

	return ctx.GetStub().PutState(id, assetJSON)
}

// ReadAsset retrieves an asset from the ledger by ID
func (s *SmartContract) ReadAsset(ctx contractapi.TransactionContextInterface, id string) (*Asset, error) {
	assetJSON, err := ctx.GetStub().GetState(id)
	if err != nil {
		return nil, fmt.Errorf("failed to read from world state: %v", err)
	}
	if assetJSON == nil {
		return nil, fmt.Errorf("the asset %s does not exist", id)
	}

	var asset Asset
	err = json.Unmarshal(assetJSON, &asset)
	if err != nil {
		return nil, err
	}

	return &asset, nil
}

// UpdateAsset updates an existing asset with new details
func (s *SmartContract) UpdateAsset(ctx contractapi.TransactionContextInterface, id string, title string, description string, participantsJSON string) error {
	exists, err := s.AssetExists(ctx, id)
	if err != nil {
		return err
	}
	if !exists {
		return fmt.Errorf("the asset %s does not exist", id)
	}

	// Parse the JSON string into a Go slice
	var participants []string
	err = json.Unmarshal([]byte(participantsJSON), &participants)
	if err != nil {
		return fmt.Errorf("error parsing participants parameter: %v", err)
	}

	// Overwriting original asset with new details
	asset := Asset{
		ID:           id,
		Title:        title,
		Description:  description,
		Participants: participants,
	}
	assetJSON, err := json.Marshal(asset)
	if err != nil {
		return fmt.Errorf("error marshaling asset: %v", err)
	}

	return ctx.GetStub().PutState(id, assetJSON)
}

// DeleteAsset removes an asset from the ledger
func (s *SmartContract) DeleteAsset(ctx contractapi.TransactionContextInterface, id string) error {
	exists, err := s.AssetExists(ctx, id)
	if err != nil {
		return err
	}
	if !exists {
		return fmt.Errorf("the asset %s does not exist", id)
	}

	return ctx.GetStub().DelState(id)
}

// AssetExists checks if an asset exists in the ledger
func (s *SmartContract) AssetExists(ctx contractapi.TransactionContextInterface, id string) (bool, error) {
	assetJSON, err := ctx.GetStub().GetState(id)
	if err != nil {
		return false, fmt.Errorf("failed to read from world state: %v", err)
	}

	return assetJSON != nil, nil
}

// GetAllAssets retrieves all assets from the ledger
func (s *SmartContract) GetAllAssets(ctx contractapi.TransactionContextInterface) ([]*Asset, error) {
	resultsIterator, err := ctx.GetStub().GetStateByRange("", "")
	if err != nil {
		return nil, err
	}
	defer resultsIterator.Close()

	var assets []*Asset
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return nil, err
		}

		var asset Asset
		err = json.Unmarshal(queryResponse.Value, &asset)
		if err != nil {
			return nil, err
		}
		assets = append(assets, &asset)
	}

	return assets, nil
}
