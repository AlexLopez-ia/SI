package domotic;

import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.Location;
import java.util.logging.Logger;

public class HouseEnv extends Environment {

    // common literals
    public static final Literal of   = Literal.parseLiteral("open(fridge)");
    public static final Literal clf  = Literal.parseLiteral("close(fridge)");
    public static final Literal gb   = Literal.parseLiteral("get(drug)");
    public static final Literal hb   = Literal.parseLiteral("hand_in(drug)");
    public static final Literal sb   = Literal.parseLiteral("sip(drug)");
    public static final Literal hob  = Literal.parseLiteral("has(owner,drug)");

    public static final Literal af   = Literal.parseLiteral("at(enfermera,fridge)");
    public static final Literal ao   = Literal.parseLiteral("at(enfermera,owner)");
    public static final Literal ad   = Literal.parseLiteral("at(enfermera,delivery)");
	
    public static final Literal oaf  = Literal.parseLiteral("at(owner,fridge)");
    public static final Literal oac1 = Literal.parseLiteral("at(owner,chair1)");
    public static final Literal oac2 = Literal.parseLiteral("at(owner,chair2)");
    public static final Literal oac3 = Literal.parseLiteral("at(owner,chair3)");
    public static final Literal oac4 = Literal.parseLiteral("at(owner,chair4)");
    public static final Literal oasf = Literal.parseLiteral("at(owner,sofa)");
    public static final Literal oad  = Literal.parseLiteral("at(owner,delivery)");

    // Literales para el gabinete de medicamentos
    public static final Literal amc = Literal.parseLiteral("at(enfermera,medcabinet)");
    public static final Literal oamc = Literal.parseLiteral("at(owner,medcabinet)");
    public static final Literal omc = Literal.parseLiteral("open(medcabinet)");
    public static final Literal cmc = Literal.parseLiteral("close(medcabinet)");

    // Literales para estar exactamente al lado del gabinete
    public static final Literal next_to_mc_enfermera = Literal.parseLiteral("next_to(enfermera,medcabinet)");
    public static final Literal next_to_mc_owner = Literal.parseLiteral("next_to(owner,medcabinet)");

    // Literal para cuando los agentes están uno al lado del otro
    public static final Literal next_to_each_other = Literal.parseLiteral("next_to(enfermera,owner)");
    public static final Literal next_to_owner_enfermera = Literal.parseLiteral("next_to(owner,enfermera)");

    static Logger logger = Logger.getLogger(HouseEnv.class.getName());

    HouseModel model; // the model of the grid

    @Override
    public void init(String[] args) {
        model = new HouseModel();

        if (args.length == 1 && args[0].equals("gui")) {
            HouseView view  = new HouseView(model);
            model.setView(view);
        }

        updatePercepts();
    }
	
    void updateAgentsPlace() {
		// get the robot location
        Location lRobot = model.getAgPos(0);
		// get the robot room location
		String RobotPlace = model.getRoom(lRobot);
		addPercept("enfermera", Literal.parseLiteral("atRoom("+RobotPlace+")"));
        addPercept("owner", Literal.parseLiteral("atRoom(enfermera,"+RobotPlace+")"));
		// get the owner location
        Location lOwner = model.getAgPos(1);
		// get the owner room location
		String OwnerPlace = model.getRoom(lOwner);
		addPercept("owner", Literal.parseLiteral("atRoom("+OwnerPlace+")"));  
        addPercept("enfermera", Literal.parseLiteral("atRoom(owner,"+OwnerPlace+")"));
		
		if (lRobot.distance(model.lDoorHome)==0 	||
			lRobot.distance(model.lDoorKit1)==0 	||
			lRobot.distance(model.lDoorKit2)==0 	||
			lRobot.distance(model.lDoorSal1)==0 	||
			lRobot.distance(model.lDoorSal2)==0 	||
			lRobot.distance(model.lDoorBath1)==0 	||
			lRobot.distance(model.lDoorBath2)==0 	||
			lRobot.distance(model.lDoorBed1)==0 	||
			lRobot.distance(model.lDoorBed2)==0 	||
			lRobot.distance(model.lDoorBed3)==0 	  ) {
			addPercept("enfermera", Literal.parseLiteral("atDoor"));
		}; 
		
		if (lOwner.distance(model.lDoorHome)==0 	||
			lOwner.distance(model.lDoorKit1)==0 	||
			lOwner.distance(model.lDoorKit2)==0 	||
			lOwner.distance(model.lDoorSal1)==0 	||
			lOwner.distance(model.lDoorSal2)==0 	||
			lOwner.distance(model.lDoorBath1)==0 	||
			lOwner.distance(model.lDoorBath2)==0 	||
			lOwner.distance(model.lDoorBed1)==0 	||
			lOwner.distance(model.lDoorBed2)==0 	||
			lOwner.distance(model.lDoorBed3)==0 	  ) {
			addPercept("owner", Literal.parseLiteral("atDoor"));
		}; 		
		
	}
      
    void updateThingsPlace() {
		// get the fridge location
		String fridgePlace = model.getRoom(model.lFridge);
		addPercept(Literal.parseLiteral("atRoom(fridge, "+fridgePlace+")"));
		String sofaPlace = model.getRoom(model.lSofa);
		addPercept(Literal.parseLiteral("atRoom(sofa, "+sofaPlace+")")); 
		String chair1Place = model.getRoom(model.lChair1);
		addPercept(Literal.parseLiteral("atRoom(chair1, "+chair1Place+")"));
		String chair2Place = model.getRoom(model.lChair2);
		addPercept(Literal.parseLiteral("atRoom(chair2, "+chair2Place+")"));
		String chair3Place = model.getRoom(model.lChair3);
		addPercept(Literal.parseLiteral("atRoom(chair3, "+chair3Place+")"));
		String chair4Place = model.getRoom(model.lChair4);
		addPercept(Literal.parseLiteral("atRoom(chair4, "+chair4Place+")"));
		String deliveryPlace = model.getRoom(model.lDeliver);
		addPercept(Literal.parseLiteral("atRoom(delivery, "+deliveryPlace+")"));
		String bed1Place = model.getRoom(model.lBed1);
		addPercept(Literal.parseLiteral("atRoom(bed1, "+bed1Place+")"));
		String bed2Place = model.getRoom(model.lBed2);
		addPercept(Literal.parseLiteral("atRoom(bed2, "+bed2Place+")"));
		String bed3Place = model.getRoom(model.lBed3);
		addPercept(Literal.parseLiteral("atRoom(bed3, "+bed3Place+")"));
	}
	                                                       
    /** creates the agents percepts based on the HouseModel */
    void updatePercepts() {
        // clear the percepts of the agents
        clearPercepts("enfermera");
        clearPercepts("owner");
		
		updateAgentsPlace();
		updateThingsPlace(); 
		
		Location lRobot = model.getAgPos(0);
		Location lOwner = model.getAgPos(1);
        
        // Añadir percepción de la ubicación del gabinete de medicamentos
        String medCabinetPlace = model.getRoom(model.lMedCabinet);
        addPercept("enfermera", Literal.parseLiteral("medication_cabinet(" + model.lMedCabinet.x + "," + model.lMedCabinet.y + ")"));
        addPercept("owner", Literal.parseLiteral("medication_cabinet(" + model.lMedCabinet.x + "," + model.lMedCabinet.y + ")"));
        addPercept(Literal.parseLiteral("atRoom(medcabinet, " + medCabinetPlace + ")"));
        addPercept("enfermera", Literal.parseLiteral("atRoom(medcabinet, " + medCabinetPlace + ")"));
        addPercept("owner", Literal.parseLiteral("atRoom(medcabinet, " + medCabinetPlace + ")"));
		
        if (lRobot.distance(model.lFridge)<2) {
            addPercept("enfermera", af);
        } 
		
        if (lOwner.distance(model.lFridge)<2) {
            addPercept("owner", oaf);
        } 
		
        // Verificar si los agentes están cerca del gabinete de medicamentos
        if (lRobot.distance(model.lMedCabinet) == 1) {
            addPercept("enfermera", next_to_mc_enfermera);
        }
        
        if (lOwner.distance(model.lMedCabinet) == 1) {
            addPercept("owner", next_to_mc_owner);
        }
        
        if (lRobot.distance(model.lMedCabinet) < 2) {
            addPercept("enfermera", amc);
        }
        
        if (lOwner.distance(model.lMedCabinet) < 2) {
            addPercept("owner", oamc);
        }
        
        if (lRobot.distance(lOwner)==1) {                                                     
            addPercept("enfermera", ao);
            addPercept("owner", next_to_owner_enfermera);
        }

        if (lRobot.distance(model.lDeliver)==1) {
            addPercept("enfermera", ad);
        }
        
        if (lRobot.distance(model.lMedCabinet)==0) {
            addPercept("enfermera", amc);
            System.out.println("[enfermera] está en el gabinete de medicamentos.");
        }

        if (lOwner.distance(model.lChair1)==0) {
            addPercept("owner", oac1);
			System.out.println("[owner] is at Chair1.");
        }

        if (lOwner.distance(model.lChair2)==0) {
            addPercept("owner", oac2);
			System.out.println("[owner] is at Chair2.");
        }

        if (lOwner.distance(model.lChair3)==0) {
            addPercept("owner", oac3);
			System.out.println("[owner] is at Chair3.");
        }

        if (lOwner.distance(model.lChair4)==0) {                            
            addPercept("owner", oac4);
			System.out.println("[owner] is at Chair4.");
        }
                                                                               
        if (lOwner.distance(model.lSofa)==0) {
            addPercept("owner", oasf);
			System.out.println("[owner] is at Sofa.");
        }

        if (lOwner.distance(model.lDeliver)==0) {
            addPercept("owner", oad);
        }

        // Añadir información sobre medicamentos si el gabinete está abierto
        if (model.medCabinetOpen) {
            addPercept("enfermera", omc);
            addPercept("owner", omc);
            
            // Añadir información sobre los medicamentos disponibles
            for (String medName : model.getMedicationNames()) {
                HouseModel.Medication med = model.getMedicationInfo(medName);
                if (med != null) {
                    Literal medInfo = Literal.parseLiteral("medication(" + medName + "," + med.quantity + ")");
                    addPercept("enfermera", medInfo);
                    addPercept("owner", medInfo);
                    
                    Literal medSchedule = Literal.parseLiteral("medication_schedule(" + medName + ",\"" + med.schedule + "\")");
                    addPercept("enfermera", medSchedule);
                    addPercept("owner", medSchedule); 
                }
            }
        }

        // Verificar posición adyacente al gabinete de medicamentos
        if (model.areAdjacent(lRobot, model.lMedCabinet)) {
            addPercept("enfermera", next_to_mc_enfermera);
        }

        if (model.areAdjacent(lOwner, model.lMedCabinet)) {
            addPercept("owner", next_to_mc_owner);
        }

        // Verificar si los agentes están uno al lado del otro
        if (model.areAdjacent(lRobot, lOwner)) {
            addPercept("enfermera", next_to_each_other);
            addPercept("owner", next_to_owner_enfermera);
        }

        // add drug "status" the percepts
        if (model.fridgeOpen) {
            addPercept("enfermera", Literal.parseLiteral("stock(drug,"+model.availableDrugs+")"));
        }
        if (model.sipCount > 0) {
            addPercept("enfermera", hob);
            addPercept("owner", hob);
        }
    }


    @Override
    public boolean executeAction(String agName, Structure action) {
        boolean result = false;
        
        try {
            if (agName.equals("enfermera")) {
                result = executeEnfermeraAction(action);
                if (result) {
                    model.setLastAgentToAct(0); // Enfermera es el agente 0
                }
            } else if (agName.equals("owner")) {
                result = executeOwnerAction(action);
                if (result) {
                    model.setLastAgentToAct(1); // Owner es el agente 1
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (result) {
            updatePercepts();
            try {
                Thread.sleep(200);
            } catch (Exception e) {}
        }
        return result;
    }
    
    private boolean executeEnfermeraAction(Structure action) {
        boolean result = false;
        
        System.out.println("["+ "enfermera" +"] doing: "+action); 
        
        if (action.getFunctor().equals("sit")) {
            String l = action.getTerm(0).toString();
            Location dest = null;
			switch (l) {
				case "chair1": dest = model.lChair1; 
				break;
				case "chair2": dest = model.lChair2;  
				break;     
				case "chair3": dest = model.lChair3; 
				break;
				case "chair4": dest = model.lChair4; 
				break;
				case "sofa": dest = model.lSofa; 
				break;
			};
			try {
				result = model.sit(0,dest);
			} catch (Exception e) {
               e.printStackTrace();
			}     
        } else if (action.equals(of)) { // of = open(fridge)
            result = model.openFridge();

        } else if (action.equals(clf)) { // clf = close(fridge)
            result = model.closeFridge();
                                                                     
        } else if (action.getFunctor().equals("move_towards")) {
            String l = action.getTerm(0).toString();
            Location dest = null;
			switch (l) {
				case "fridge": dest = model.lFridge; 
				break;
				case "owner": dest = model.getAgPos(1);  
				break;     
				case "delivery": dest = model.lDeliver;  
				break;     
				case "chair1": dest = model.lChair1; 
				break;
				case "chair2": dest = model.lChair2; 
				break;
				case "chair3": dest = model.lChair3; 
				break;
				case "chair4": dest = model.lChair4; 
				break;
				case "sofa": dest = model.lSofa; 
				break;
				case "washer": dest = model.lWasher; 
				break;
				case "table": dest = model.lTable; 
				break;
				case "doorBed1": dest = model.lDoorBed1; 
				break;            
				case "doorBed2": dest = model.lDoorBed2; 
				break;
				case "doorBed3": dest = model.lDoorBed3; 
				break;
				case "doorKit1": dest = model.lDoorKit1; 
				break;
				case "doorKit2": dest = model.lDoorKit2; 
				break;
				case "doorSal1": dest = model.lDoorSal1; 
				break;
				case "doorSal2": dest = model.lDoorSal2; 
				break;
				case "doorBath1": dest = model.lDoorBath1; 
				break;
				case "doorBath2": dest = model.lDoorBath2;                  
				break; 
				case "medcabinet": dest = model.lMedCabinet;
				break;
            }
            try {
                result = model.moveTowards(0, dest);
            } catch (Exception e) {
                e.printStackTrace();
            }     

        } else if (action.equals(gb)) {
            result = model.getDrug();

        } else if (action.equals(hb)) {
            result = model.handInDrug();

        } else if (action.equals(sb)) {
            result = model.sipDrug();

        } else if (action.equals(omc)) { // open medication cabinet
            result = model.openMedCabinet();
            
        } else if (action.equals(cmc)) { // close medication cabinet
            result = model.closeMedCabinet();
            
        } else if (action.getFunctor().equals("take_medication")) {
            String medName = action.getTerm(0).toString();
            result = model.takeMedication(medName);
            
        } /*else if (action.getFunctor().equals("add_medication")) {
            String medName = action.getTerm(0).toString();
            int quantity = (int)((NumberTerm)action.getTerm(1)).solve();
            result = model.addMedication(medName, quantity);
            
        }*/ else if (action.getFunctor().equals("update_medication_schedule")) {
            String medName = action.getTerm(0).toString();
            String schedule = action.getTerm(1).toString();
            result = model.updateMedicationSchedule(medName, schedule);

        } else if (action.getFunctor().equals("position_next_to")) {
            String target = action.getTerm(0).toString();
            
            try {
                result = model.moveToAdjacentPosition(0, target);
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }

        } else if (action.getFunctor().equals("deliver")) {
            // wait 4 seconds to finish "deliver"
            try {
                result = model.addDrug( (int)((NumberTerm)action.getTerm(1)).solve());
                Thread.sleep(4000);
            } catch (Exception e) {
                logger.info("Failed to execute action deliver!"+e);
            }

        } else {
            logger.info("Failed to execute action "+action);
        }

        return result;
    }
    
    private boolean executeOwnerAction(Structure action) {
        boolean result = false;
        
        System.out.println("["+ "owner" +"] doing: "+action); 
        
        if (action.getFunctor().equals("sit")) {
            String l = action.getTerm(0).toString();
            Location dest = null;
			switch (l) {
				case "chair1": dest = model.lChair1; 
				break;
				case "chair2": dest = model.lChair2;  
				break;     
				case "chair3": dest = model.lChair3; 
				break;
				case "chair4": dest = model.lChair4; 
				break;
				case "sofa": dest = model.lSofa; 
				break;
			};
			try {
				result = model.sit(1,dest);
			} catch (Exception e) {
               e.printStackTrace();
			}     
        } else if (action.getFunctor().equals("move_towards")) {
            String l = action.getTerm(0).toString();
            Location dest = null;
			switch (l) {
				case "fridge": dest = model.lFridge; 
				break;
				case "enfermera": dest = model.getAgPos(0);  
				break;     
				case "delivery": dest = model.lDeliver;  
				break;     
				case "chair1": dest = model.lChair1; 
				break;
				case "chair2": dest = model.lChair2; 
				break;
				case "chair3": dest = model.lChair3; 
				break;
				case "chair4": dest = model.lChair4; 
				break;
				case "sofa": dest = model.lSofa; 
				break;
				case "washer": dest = model.lWasher; 
				break;
				case "table": dest = model.lTable; 
				break;
				case "doorBed1": dest = model.lDoorBed1; 
				break;            
				case "doorBed2": dest = model.lDoorBed2; 
				break;
				case "doorBed3": dest = model.lDoorBed3; 
				break;
				case "doorKit1": dest = model.lDoorKit1; 
				break;
				case "doorKit2": dest = model.lDoorKit2; 
				break;
				case "doorSal1": dest = model.lDoorSal1; 
				break;
				case "doorSal2": dest = model.lDoorSal2; 
				break;
				case "doorBath1": dest = model.lDoorBath1; 
				break;
				case "doorBath2": dest = model.lDoorBath2;                  
				break;
                case "medcabinet": dest = model.lMedCabinet;
                break;
            }
            try {
                result = model.moveTowards(1, dest);
            } catch (Exception e) {
                e.printStackTrace();
            }     

        } else if (action.equals(omc)) { // open medication cabinet
            result = model.openMedCabinet();
            
        } else if (action.equals(cmc)) { // close medication cabinet
            result = model.closeMedCabinet();
            
        } else if (action.getFunctor().equals("take_medication")) {
            String medName = action.getTerm(0).toString();
            result = model.takeMedication(medName);
            
        } /*else if (action.getFunctor().equals("add_medication")) {
            String medName = action.getTerm(0).toString();
            int quantity = (int)((NumberTerm)action.getTerm(1)).solve();
            result = model.addMedication(medName, quantity);
            
        }*/ else if (action.getFunctor().equals("update_medication_schedule")) {
            String medName = action.getTerm(0).toString();
            String schedule = action.getTerm(1).toString();
            result = model.updateMedicationSchedule(medName, schedule);

        } else if (action.getFunctor().equals("position_next_to")) {
            String target = action.getTerm(0).toString();
            
            try {
                result = model.moveToAdjacentPosition(1, target);
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }

        } else {
            logger.info("Failed to execute action "+action);
        }

        return result;
    }
}
