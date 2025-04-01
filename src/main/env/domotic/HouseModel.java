package domotic;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;
import jason.environment.grid.Area;
import jason.asSyntax.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/** class that implements the Model of Domestic Robot application */
public class HouseModel extends GridWorldModel {

    // constants for the grid objects

    public static final int COLUMN  =    4;
    public static final int CHAIR  	=    8;
    public static final int SOFA  	=   16;
    public static final int FRIDGE 	=   32;
    public static final int WASHER 	=   64;
	public static final int DOOR 	=  128;                                       
	public static final int CHARGER =  256;
    public static final int TABLE  	=  512;
    public static final int BED	   	= 1024;
    public static final int WALLV   = 2048;
    public static final int MEDCABINET = 4096; // Gabinete de medicamentos

    // the grid size                                                     
    public static final int GSize = 12;        //Cells
	public final int GridSize = 1080;    //Width
                          
    boolean fridgeOpen   = false; // whether the fridge is open                                   
    boolean carryingDrug = false; // whether the robot is carrying drug
    boolean medCabinetOpen = false; // whether the medication cabinet is open
    boolean medCabinetStateChanged = false; // whether the cabinet state has changed
    String lastMedicationTaken = null; // último medicamento tomado
    int lastMedicationQuantity = 0; // cantidad restante del último medicamento tomado
    int lastAgentToAct = -1; // último agente que realizó una acción
    int sipCount        = 0; // how many sip the owner did
    int availableDrugs  = 2; // how many drugs are available

	// Initialization of the objects Location on the domotic home scene 
    Location lSofa	 	= new Location(GSize/2, GSize-2);
    Location lChair1  	= new Location(GSize/2+2, GSize-3);
    Location lChair3 	= new Location(GSize/2-1, GSize-3);
    Location lChair2 	= new Location(GSize/2+1, GSize-4); 
    Location lChair4 	= new Location(GSize/2, GSize-4); 
    Location lDeliver 	= new Location(0, GSize-1);
    Location lWasher 	= new Location(GSize/3, 0);	
    Location lFridge 	= new Location(2, 0);
    Location lTable  	= new Location(GSize/2, GSize-3);
	Location lBed2		= new Location(GSize+2, 0);
	Location lBed3		= new Location(GSize*2-3,0);
	Location lBed1		= new Location(GSize+1, GSize*3/4);
    Location lMedCabinet = new Location(0, 3); // Ubicación del gabinete de medicamentos (en la cocina)

	// Initialization of the doors location on the domotic home scene 
	Location lDoorHome 	= new Location(0, GSize-1);  
	Location lDoorKit1	= new Location(0, GSize/2);
	Location lDoorKit2	= new Location(GSize/2+1, GSize/2-1); 
	Location lDoorSal1	= new Location(GSize/4, GSize-1);  
	Location lDoorSal2	= new Location(GSize+1, GSize/2);
	Location lDoorBed1	= new Location(GSize-1, GSize/2);
	Location lDoorBath1	= new Location(GSize-1, GSize/4+1);
	Location lDoorBed3	= new Location(GSize*2-1, GSize/4+1); 	
	Location lDoorBed2	= new Location(GSize+1, GSize/4+1); 	
	Location lDoorBath2	= new Location(GSize*2-4, GSize/2+1); 	
	
	// Initialization of the area modeling the home rooms      
	Area kitchen 	= new Area(0, 0, GSize/2+1, GSize/2-1);
	Area livingroom	= new Area(GSize/3, GSize/2+1, GSize, GSize-1);
	Area bath1	 	= new Area(GSize/2+2, 0, GSize-1, GSize/3);
	Area bath2	 	= new Area(GSize*2-3, GSize/2+1, GSize*2-1, GSize-1);
	Area bedroom1	= new Area(GSize+1, GSize/2+1, GSize*2-4, GSize-1);
	Area bedroom2	= new Area(GSize, 0, GSize*3/4-1, GSize/3);
	Area bedroom3	= new Area(GSize*3/4, 0, GSize*2-1, GSize/3);
	Area hall		= new Area(0, GSize/2+1, GSize/4, GSize-1);
	Area hallway	= new Area(GSize/2+2, GSize/2-1, GSize*2-1, GSize/2);
	/*
	Modificar el modelo para que la casa sea un conjunto de habitaciones
	Dar un codigo a cada habitación y vincular un Area a cada habitación
	Identificar los objetos de manera local a la habitación en que estén
	Crear un método para la identificación del tipo de agente existente
	Identificar objetos globales que precisen de un único identificador
	*/
	
    public HouseModel() {
        // create a GSize x 2GSize grid with 3 mobile agent
        super(2*GSize, GSize, 2);
                                                                           
        // initial location of robot (column 3, line 3)
        // ag code 0 means the robot
        setAgPos(0, 19, 10);  
		setAgPos(1, 23, 8);
		//setAgPos(2, GSize*2-1, GSize*3/5);

		// Do a new method to create literals for each object placed on
		// the model indicating their nature to inform agents their existence
		
        // initial location of fridge and owner
        add(FRIDGE, lFridge); 
		add(WASHER, lWasher); 
		add(DOOR,   lDeliver); 
		add(SOFA,   lSofa);
		add(CHAIR,  lChair2);
		add(CHAIR,  lChair3);
		add(CHAIR,  lChair4);
        add(CHAIR,  lChair1);  
        add(TABLE,  lTable);  
		add(BED,	lBed1);
		add(BED,	lBed2);
		add(BED,	lBed3);
        add(MEDCABINET, lMedCabinet); // Añadir gabinete de medicamentos
		
        // Inicializar medicamentos disponibles
        medications.put("paracetamol", new Medication("paracetamol", 30, "8h,16h"));
        medications.put("ibuprofeno", new Medication("ibuprofeno", 20, "12h"));
        medications.put("aspirina", new Medication("aspirina", 25, "20h"));
        medications.put("vitamina", new Medication("vitamina", 60, "8h"));
        medications.put("insulina", new Medication("insulina", 10, "7h,19h"));

		addWall(GSize/2+1, 0, GSize/2+1, GSize/2-2);  		
		add(DOOR, lDoorKit2);                              
		//addWall(GSize/2+1, GSize/2-1, GSize/2+1, GSize-2);  
		add(DOOR, lDoorSal1); 

		addWall(GSize/2+1, GSize/4+1, GSize-2, GSize/4+1);   
		//addWall(GSize+1, GSize/4+1, GSize*2-1, GSize/4+1);   
 		add(DOOR, lDoorBath1); 
		//addWall(GSize+1, GSize*2/5+1, GSize*2-2, GSize*2/5+1);   
		addWall(GSize+2, GSize/4+1, GSize*2-2, GSize/4+1);   
		addWall(GSize*2-6, 0, GSize*2-6, GSize/4);
		add(DOOR, lDoorBed1); 
		
		addWall(GSize, 0, GSize, GSize/4+1);  
		//addWall(GSize+1, GSize/4+1, GSize, GSize/4+1);  
 		add(DOOR, lDoorBed2); 
		
		addWall(1, GSize/2, GSize-1, GSize/2);            
		add(DOOR, lDoorKit1);                
		add(DOOR, lDoorSal2);

		addWall(GSize/4, GSize/2+1, GSize/4, GSize-2);            
		
		addWall(GSize, GSize/2, GSize, GSize-1);  
		addWall(GSize*2-4, GSize/2+2, GSize*2-4, GSize-1);  
		addWall(GSize+2, GSize/2, GSize*2-1, GSize/2);  
 		add(DOOR, lDoorBed3);  
 		add(DOOR, lDoorBath2);  
		
     }
	

	 String getRoom (Location thing){  
		
		String byDefault = "kitchen";

		if (bath1.contains(thing)){
			byDefault = "bath1";
		};
		if (bath2.contains(thing)){
			byDefault = "bath2";
		};
		if (bedroom1.contains(thing)){
			byDefault = "bedroom1";
		};
		if (bedroom2.contains(thing)){
			byDefault = "bedroom2";
		};
		if (bedroom3.contains(thing)){
			byDefault = "bedroom3";
		};
		if (hallway.contains(thing)){
			byDefault = "hallway";
		};
		if (livingroom.contains(thing)){
			byDefault = "livingroom";
		};
		if (hall.contains(thing)){
			byDefault = "hall";
		};
		return byDefault;
	}

	boolean sit(int Ag, Location dest) { 
		Location loc = getAgPos(Ag);
		if (loc.isNeigbour(dest)) {
			setAgPos(Ag, dest);
		};
		return true;
	}

	boolean openFridge() {
        if (!fridgeOpen) {
            fridgeOpen = true;
            return true;
        } else {
            return false;
        }
    }

    boolean closeFridge() {
        if (fridgeOpen) {
            fridgeOpen = false;
            return true;
        } else {
            return false;
        }
    }   

    boolean canMoveTo(int Ag, int x, int y) {
		// Verificar que la posición está dentro de los límites del grid
		if(!(x<0 || x>=24 || y<0 || y>=12))
		{
			if(!(x==7 && y==9))
			{
				// Verificar explícitamente si la posición está en una cama o cerca de ella
				// Las camas ocupan 2x2 celdas según el código de dibujo
				if ((x >= lBed1.x && x <= lBed1.x + 1 && y >= lBed1.y && y <= lBed1.y + 1) ||
					(x >= lBed2.x && x <= lBed2.x + 1 && y >= lBed2.y && y <= lBed2.y + 1) ||
					(x >= lBed3.x && x <= lBed3.x + 1 && y >= lBed3.y && y <= lBed3.y + 1)) {
					return false;
				}
				
				// Continuar con la lógica original
				if (Ag < 1) {
					return (isFree(x, y) && !hasObject(TABLE, x, y) &&
							!hasObject(SOFA, x, y) && !hasObject(CHAIR, x, y) && !hasObject(FRIDGE, x, y) &&
							!hasObject(MEDCABINET, x, y) && !hasObject(WASHER, x, y));
				} else {
					return (isFree(x, y) && !hasObject(TABLE, x, y) && !hasObject(FRIDGE, x, y)
							&& !hasObject(MEDCABINET, x, y));
				}
			}
		}
		
		return false;
	}

	boolean moveOneStep(int Ag, Location dest) {
	    Location currentPos = getAgPos(Ag);
	    Location nextPos = new Location(currentPos.x, currentPos.y);
	    
	    // Calcular la dirección preferida (la que más acerca al destino)
	    int dx = Integer.compare(dest.x, currentPos.x); // -1, 0, o 1
	    int dy = Integer.compare(dest.y, currentPos.y); // -1, 0, o 1
	    
	    // Decidir si moverse horizontal o verticalmente
	    boolean moveHorizontal = false;
	    
	    // Si la distancia horizontal es mayor o igual que la vertical, priorizar movimiento horizontal
	    if (Math.abs(dest.x - currentPos.x) >= Math.abs(dest.y - currentPos.y)) {
	        moveHorizontal = true;
	    } else {
	        moveHorizontal = false;
	    }
	    
	    // Intentar moverse en la dirección prioritaria primero
	    if (moveHorizontal && dx != 0) {
	        // Intentar moverse horizontalmente
	        nextPos.x = currentPos.x + dx;
	        if (canMoveTo(Ag, nextPos.x, nextPos.y)) {
	            setAgPos(Ag, nextPos);
	            return true;
	        }
	    } else if (!moveHorizontal && dy != 0) {
	        // Intentar moverse verticalmente
	        nextPos.y = currentPos.y + dy;
	        if (canMoveTo(Ag, nextPos.x, nextPos.y)) {
	            setAgPos(Ag, nextPos);
	            return true;
	        }
	    }
	    
	    // Si no se puede mover en la dirección prioritaria, intentar la otra dirección
	    if (moveHorizontal && dy != 0) {
	        // Intentar moverse verticalmente como segunda opción
	        nextPos = new Location(currentPos.x, currentPos.y + dy);
	        if (canMoveTo(Ag, nextPos.x, nextPos.y)) {
	            setAgPos(Ag, nextPos);
	            return true;
	        }
	    } else if (!moveHorizontal && dx != 0) {
	        // Intentar moverse horizontalmente como segunda opción
	        nextPos = new Location(currentPos.x + dx, currentPos.y);
	        if (canMoveTo(Ag, nextPos.x, nextPos.y)) {
	            setAgPos(Ag, nextPos);
	            return true;
	        }
	    }
	    
	    // Si no se pudo mover en ninguna dirección preferida, intentar cualquier dirección disponible
	    int[][] directions = {{1,0}, {-1,0}, {0,1}, {0,-1}}; // derecha, izquierda, abajo, arriba
	    
	    for (int[] dir : directions) {
	        nextPos = new Location(currentPos.x + dir[0], currentPos.y + dir[1]);
	        if (canMoveTo(Ag, nextPos.x, nextPos.y)) {
	            setAgPos(Ag, nextPos);
	            return true;
	        }
	    }
	    
	    // No se pudo mover en ninguna dirección
	    return false;
	}

	boolean moveTowards(int Ag, Location dest) {
	    Location currentPos = getAgPos(Ag);
	    
	    // Si ya estamos en el destino, no hay que moverse
	    if (currentPos.equals(dest)) {
	        return true;
	    }
	    
	    // Utilizar el nuevo método para mover solo una casilla
	    return moveOneStep(Ag, dest);
	}

    boolean getDrug() {
        // Verificar si el último agente que actuó está cerca del gabinete
        int ag = getLastAgentToAct();
        System.out.println("Intentando obtener medicamento. Último agente: " + ag);
        
        if (ag >= 0) {
            Location agPos = getAgPos(ag);
            System.out.println("Posición del agente: " + agPos.x + "," + agPos.y);
            System.out.println("Posición del gabinete: " + lMedCabinet.x + "," + lMedCabinet.y);
            System.out.println("Distancia al gabinete: " + agPos.distance(lMedCabinet));
            
            if (agPos.distance(lMedCabinet) <= 1) {
                if (medCabinetOpen && !carryingDrug) {
                    // Verificar si hay medicamentos disponibles
                    boolean hayMedicamentos = false;
                    for (Medication med : medications.values()) {
                        if (med.quantity > 0) {
                            hayMedicamentos = true;
                            break;
                        }
                    }
                    
                    if (hayMedicamentos) {
                        carryingDrug = true;
                        System.out.println("El agente " + ag + " ha obtenido un medicamento del gabinete.");
                        return true;
                    } else {
                        System.out.println("No hay medicamentos disponibles en el gabinete.");
                    }
                } else {
                    if (!medCabinetOpen) {
                        System.out.println("El gabinete de medicamentos está cerrado.");
                    }
                    if (carryingDrug) {
                        System.out.println("El agente ya está llevando un medicamento.");
                    }
                }
            } else {
                System.out.println("El agente " + ag + " no está lo suficientemente cerca del gabinete de medicamentos para obtener un medicamento.");
            }
        } else {
            System.out.println("No se ha registrado ningún agente que intente obtener un medicamento.");
        }
        return false;
    }

    boolean addDrug(int n) {
        availableDrugs += n;
        //if (view != null)
        //    view.update(lFridge.x,lFridge.y);
        return true;
    }

    boolean handInDrug() {
        // Verificar si el último agente que actuó está cerca del propietario
        int ag = getLastAgentToAct();
        System.out.println("Intentando entregar medicamento. Último agente: " + ag);
        
        if (ag >= 0) {
            Location agPos = getAgPos(ag);
            Location ownerPos = getAgPos(1); // El propietario es el agente 1
            
            System.out.println("Posición del agente: " + agPos.x + "," + agPos.y);
            System.out.println("Posición del propietario: " + ownerPos.x + "," + ownerPos.y);
            System.out.println("Distancia al propietario: " + agPos.distance(ownerPos));
            
            if (agPos.distance(ownerPos) <= 1) {
                if (carryingDrug) {
                    sipCount = 10;
                    carryingDrug = false;
                    System.out.println("El agente " + ag + " ha entregado el medicamento al propietario.");
                    return true;
                } else {
                    System.out.println("El agente no está llevando ningún medicamento para entregar.");
                }
            } else {
                System.out.println("El agente " + ag + " no está lo suficientemente cerca del propietario para entregarle el medicamento.");
            }
        } else {
            System.out.println("No se ha registrado ningún agente que intente entregar un medicamento.");
        }
        return false;
    }

    boolean sipDrug() {
        if (sipCount > 0) {
            sipCount--;
            //if (view != null)
                //view.update(lOwner.x,lOwner.y);
            return true;
        } else {
            return false;
        }
    }
    
    // Representación de medicamentos con clase interna
    class Medication {
        String name;
        int quantity;
        String schedule; // Horario de toma
        
        public Medication(String name, int quantity, String schedule) {
            this.name = name;
            this.quantity = quantity;
            this.schedule = schedule;
        }
    }
    
    // Lista de medicamentos en el gabinete
    java.util.Map<String, Medication> medications = new java.util.HashMap<>();

    // Método para abrir el gabinete de medicamentos
    boolean openMedCabinet() {
        // Verificar si el último agente que actuó está cerca del gabinete
        int ag = getLastAgentToAct();
        System.out.println("Intentando abrir el gabinete de medicamentos. Último agente: " + ag);
        
        if (ag >= 0) {
            Location agPos = getAgPos(ag);
            System.out.println("Posición del agente: " + agPos.x + "," + agPos.y);
            System.out.println("Posición del gabinete: " + lMedCabinet.x + "," + lMedCabinet.y);
            System.out.println("Distancia al gabinete: " + agPos.distance(lMedCabinet));
            
            if (agPos.distance(lMedCabinet) <= 1) {
                System.out.println("El agente " + ag + " está lo suficientemente cerca del gabinete.");
                if (!medCabinetOpen) {
                    medCabinetOpen = true;
                    medCabinetStateChanged = true;
                    System.out.println("Gabinete de medicamentos abierto correctamente.");
                    return true;
                } else {
                    System.out.println("El gabinete ya está abierto, consideramos la acción como exitosa.");
                    return true; // Retornar true aunque el gabinete ya esté abierto
                }
            } else {
                System.out.println("El agente " + ag + " no está lo suficientemente cerca del gabinete de medicamentos para abrirlo.");
            }
        } else {
            System.out.println("No se ha registrado ningún agente que intente abrir el gabinete.");
        }
        return false;
    }

    // Método para cerrar el gabinete de medicamentos
    boolean closeMedCabinet() {
        // Verificar si el último agente que actuó está cerca del gabinete
        int ag = getLastAgentToAct();
        if (ag >= 0) {
            Location agPos = getAgPos(ag);
            if (agPos.distance(lMedCabinet) <= 1) {
                if (medCabinetOpen) {
                    medCabinetOpen = false;
                    medCabinetStateChanged = true;
                    System.out.println("Gabinete de medicamentos cerrado correctamente.");
                    return true;
                } else {
                    System.out.println("El gabinete ya está cerrado, consideramos la acción como exitosa.");
                    return true; // Retornar true aunque el gabinete ya esté cerrado
                }
            } else {
                System.out.println("El agente " + ag + " no está lo suficientemente cerca del gabinete de medicamentos para cerrarlo.");
            }
        }
        return false;
    }
    
    // Método para obtener la información de un medicamento
    Medication getMedicationInfo(String medName) {
        return medications.get(medName);
    }
    
    // Método para tomar una unidad de un medicamento
    boolean takeMedication(String medName) {
        // Verificar si el último agente que actuó está cerca del gabinete
        int ag = getLastAgentToAct();
        if (ag >= 0) {
            Location agPos = getAgPos(ag);
            if (agPos.distance(lMedCabinet) <= 1) {
                Medication med = medications.get(medName);
                if (med != null && med.quantity > 0 && medCabinetOpen) {
                    med.quantity--;
                    
                    // Guardar información para la visualización
                    lastMedicationTaken = medName;
                    lastMedicationQuantity = med.quantity;
                    
                    System.out.println("El agente " + ag + " ha tomado el medicamento " + medName + ". Quedan " + med.quantity + " unidades.");
                    return true;
                } else {
                    if (med == null) {
                        System.out.println("El medicamento " + medName + " no existe en el gabinete.");
                    } else if (med.quantity <= 0) {
                        System.out.println("No quedan unidades del medicamento " + medName + ".");
                    } else if (!medCabinetOpen) {
                        System.out.println("El gabinete de medicamentos está cerrado.");
                    }
                }
            } else {
                System.out.println("El agente " + ag + " no está lo suficientemente cerca del gabinete de medicamentos para tomar un medicamento.");
            }
        }
        return false;
    }
    
    // Método para añadir medicamento al gabinete
    boolean addMedication(String medName, int quantity) {
        Medication med = medications.get(medName);
        if (med != null) {
            med.quantity += quantity;
        } else {
            medications.put(medName, new Medication(medName, quantity, ""));
        }
        return true;
    }
    
    // Método para actualizar el horario de un medicamento
    boolean updateMedicationSchedule(String medName, String schedule) {
        Medication med = medications.get(medName);
        if (med != null) {
            med.schedule = schedule;
            return true;
        }
        return false;
    }
    
    // Método para obtener los nombres de todos los medicamentos
    java.util.Set<String> getMedicationNames() {
        return medications.keySet();
    }
    
    /**
     * Obtiene el mapa completo de medicamentos
     */
    java.util.Map<String, Medication> getMedicationMap() {
        return medications;
    }
    
    /**
     * Obtiene y luego resetea el último medicamento tomado
     */
    String getAndResetLastMedicationTaken() {
        String result = lastMedicationTaken;
        lastMedicationTaken = null;
        return result;
    }
    
    /**
     * Obtiene la cantidad actual del último medicamento tomado
     */
    int getLastMedicationQuantity() {
        return lastMedicationQuantity;
    }
    
    /**
     * Obtiene y resetea si el estado del gabinete cambió
     */
    boolean getMedCabinetStateChanged() {
        boolean result = medCabinetStateChanged;
        medCabinetStateChanged = false;
        return result;
    }
    
    /**
     * Establece el último agente que actuó
     */
    void setLastAgentToAct(int ag) {
        this.lastAgentToAct = ag;
    }
    
    /**
     * Obtiene el último agente que actuó
     */
    int getLastAgentToAct() {
        return lastAgentToAct;
    }

    /**
     * Verifica si dos ubicaciones son adyacentes (están una al lado de la otra)
     * Las ubicaciones son adyacentes si están a distancia 1 en posición horizontal o vertical
     */
    boolean areAdjacent(Location loc1, Location loc2) {
        return loc1.distance(loc2) == 1;
    }
    
    /**
     * Verifica si un agente está adyacente al gabinete de medicamentos
     */
    boolean isNextToMedCabinet(int ag) {
        Location agentLoc = getAgPos(ag);
        return areAdjacent(agentLoc, lMedCabinet);
    }
    
    /**
     * Verifica si un agente está adyacente a otro agente
     */
    boolean isNextToAgent(int ag1, int ag2) {
        Location loc1 = getAgPos(ag1);
        Location loc2 = getAgPos(ag2);
        return areAdjacent(loc1, loc2);
    }
    
    /**
     * Mueve al agente a una posición adyacente al objetivo especificado
     * @param ag El índice del agente a mover
     * @param target El objetivo ("medcabinet", "owner" o "enfermera")
     * @return true si se pudo mover, false en caso contrario
     */
    boolean moveToAdjacentPosition(int ag, String target) {
        Location targetLoc = null;
        
        if (target.equals("medcabinet")) {
            targetLoc = lMedCabinet;
        } else if (target.equals("owner")) {
            targetLoc = getAgPos(1); // Posición del owner
        } else if (target.equals("enfermera")) {
            targetLoc = getAgPos(0); // Posición de la enfermera
        }
        
        if (targetLoc == null) {
            return false;
        }
        
        // Si ya está adyacente, no necesita moverse
        Location agentLoc = getAgPos(ag);
        if (areAdjacent(agentLoc, targetLoc)) {
            return true;
        }
        
        // Intentar moverse a una posición adyacente
        // Probar las cuatro direcciones posibles (arriba, abajo, izquierda, derecha)
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        
        for (int[] dir : directions) {
            int newX = targetLoc.x + dir[0];
            int newY = targetLoc.y + dir[1];
            
            // Verificar si la posición es válida y el agente puede moverse allí
            if (isFree(newX, newY) && canMoveTo(ag, newX, newY)) {
                // Mover al agente a esta posición
                setAgPos(ag, newX, newY);
                return true;
            }
        }
        
        // Si no se pudo mover a una posición adyacente directamente,
        // intentar acercarse lo más posible
        return moveOneStep(ag, targetLoc);
    }
}
