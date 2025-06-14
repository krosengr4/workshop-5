import com.pluralsight.utils.*;

import java.util.ArrayList;

public class Dealership {

    String name;
    String address;
    String phoneNumber;
    ArrayList<Vehicle> inventory;

    //Default constructor
    public Dealership() {
    }

    //Overloaded constructor
    public Dealership(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        inventory = new ArrayList<>();
    }

    //region getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    //endregion

    //Method to search vehicles by a price range, and return array list of all vehicles within that price range
    public ArrayList<Vehicle> getVehiclesByPrice(int min, int max) {

        inventory = DealershipFileManager.getInventory();
        ArrayList<Vehicle> vehiclesByPrice = new ArrayList<>();

        for (Vehicle vehicle : inventory) {
            if (vehicle.getPrice() >= min && vehicle.getPrice() <= max) {
                vehiclesByPrice.add(vehicle);
            }
        }

        return vehiclesByPrice;
    }

    //Method to search vehicles by Make and model, and return array list of all vehicles that match
    public ArrayList<Vehicle> getVehicleByMakeModel(String make, String model) {

        inventory = DealershipFileManager.getInventory();
        ArrayList<Vehicle> vehiclesByMakeModel = new ArrayList<>();

        for (Vehicle vehicle : inventory) {
            if (vehicle.getMake().equalsIgnoreCase(make) && vehicle.getModel().equalsIgnoreCase(model)) {
                vehiclesByMakeModel.add(vehicle);
            }
        }

        return vehiclesByMakeModel;
    }

    //Method to search vehicles by year range, and return array list of all vehicles in that range
    public ArrayList<Vehicle> getVehiclesByYear(int min, int max) {
        inventory = DealershipFileManager.getInventory();
        ArrayList<Vehicle> vehiclesByYear = new ArrayList<>();

        for (Vehicle vehicle : inventory) {
            if (vehicle.getYear() >= min && vehicle.getYear() <= max) {
                vehiclesByYear.add(vehicle);
            }
        }
        return vehiclesByYear;
    }

    //Method to search vehicles by color, and return array list of all vehicles that match
    public ArrayList<Vehicle> getVehiclesByColor(String color) {
        inventory = DealershipFileManager.getInventory();
        ArrayList<Vehicle> vehiclesByColor = new ArrayList<>();

        for (Vehicle v : inventory) {
            if (color.equalsIgnoreCase(v.getColor())) {
                vehiclesByColor.add(v);
            }
        }
        return vehiclesByColor;
    }

    //Method to search vehicles by mileage range, and return array list of all vehicles in that range
    public ArrayList<Vehicle> getVehiclesByMileage(int min, int max) {
        inventory = DealershipFileManager.getInventory();
        ArrayList<Vehicle> vehiclesByMileage = new ArrayList<>();

        for (Vehicle v : inventory) {
            if (v.getOdometer() >= min && v.getOdometer() <= max) {
                vehiclesByMileage.add(v);
            }
        }
        return vehiclesByMileage;
    }

    //Method to search vehicles by Vehicle type, and return array list of all vehicles that match
    public ArrayList<Vehicle> getVehiclesByType(String type) {
        inventory = DealershipFileManager.getInventory();
        ArrayList<Vehicle> vehiclesByType = new ArrayList<>();

        for (Vehicle v : inventory) {
            if (v.getVehicleType().equalsIgnoreCase(type)) {
                vehiclesByType.add(v);
            }
        }
        return vehiclesByType;
    }

    //Method that returns all vehicles in the inventory
    public ArrayList<Vehicle> getAllVehicles() {
        inventory = DealershipFileManager.getInventory();
        return inventory;
    }

    //Method to add a vehicle to the inventory, and write it to the car-inventory file
    public void addVehicle(Vehicle vehicle) {
        inventory = DealershipFileManager.getInventory();

        inventory.add(vehicle);
        DealershipFileManager.writeToInventory(inventory);
    }

    //Method that removes a vehicles in the inventory, and removes it from the car-inventory file
    public void removeVehicle(int VIN) {

        inventory = DealershipFileManager.getInventory();
        boolean isCarFound = false;

        for (Vehicle v : inventory) {
            if (v.getVin() == VIN) {
                inventory.remove(v);
                isCarFound = true;
                break;
            }
        }

        if (isCarFound) {
            System.out.println("Success! Car was removed");
            DealershipFileManager.writeToInventory(inventory);
        } else {
            System.err.println("Could not find car with that VIN...");
        }

        UserIO.pauseApp();
    }

    //Method that creates a new sell contract and calls write to contracts file method
    public void sellVehicle(String date, String customerName, String customerEmail, boolean isFinance, int VIN) {

        inventory = DealershipFileManager.getInventory();
        SalesContract salesContract = new SalesContract();
        boolean isCarFound = false;

        for (Vehicle v : inventory) {
            if (VIN == v.getVin()) {
                salesContract = new SalesContract(date, customerName, customerEmail, v, isFinance);
                isCarFound = true;
                inventory.remove(v);
                break;
            }
        }
        if (isCarFound) {
            DealershipFileManager.writeToInventory(inventory);
            System.out.println("\nSuccess! Vehicle was sold!");
            ContractFileManager.writeToContractsFile(salesContract);
        } else {
            System.err.println("ERROR! We could not find a car with that VIN!");
        }
    }

    //Method that creates a new lease contract and calls write to contracts file method
    public void leaseVehicle(String date, String customerName, String customerEmail, int VIN) {

        inventory = DealershipFileManager.getInventory();
        LeaseContract leaseContract = new LeaseContract();
        boolean isCarFound = false;

        for(Vehicle v : inventory) {
            if (VIN == v.getVin()) {
                leaseContract = new LeaseContract(date, customerName, customerEmail, v);
                isCarFound = true;
                inventory.remove(v);
                break;
            }
        }
        if(isCarFound) {
            DealershipFileManager.writeToInventory(inventory);
            System.out.println("\nSuccess! Vehicle was leased!");
            ContractFileManager.writeToContractsFile(leaseContract);
        } else {
            System.err.println("ERROR! We could not find a car with that VIN!");
        }
    }

    //Method that gets and returns all contracts
    public ArrayList<Contract> getAllContracts() {
        ArrayList<Contract> allContracts = new ArrayList<>();
        ArrayList<SalesContract> salesContracts = ContractFileManager.readSaleContracts();
        ArrayList<LeaseContract> leaseContracts = ContractFileManager.readLeaseContracts();

        allContracts.addAll(salesContracts);
        allContracts.addAll(leaseContracts);

        return allContracts;
    }

    //Method that gets and returns sales contracts
    public ArrayList<Contract> getSalesContracts() {
        ArrayList<Contract> contracts= new ArrayList<>();
        ArrayList<SalesContract> salesContracts = ContractFileManager.readSaleContracts();

        contracts.addAll(salesContracts);

        return contracts;
    }

    //Method that gets and returns lease contracts
    public ArrayList<Contract> getLeaseContracts() {
        ArrayList<Contract> contracts = new ArrayList<>();
        ArrayList<LeaseContract> leaseContracts = ContractFileManager.readLeaseContracts();

        contracts.addAll(leaseContracts);
        return contracts;
    }
}

