package domotic;

import jason.environment.grid.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.net.URL;
import java.nio.file.Paths;

import javax.swing.ImageIcon;

/** class that implements the View of Domestic Robot application */
public class HouseView extends GridWorldView {

    HouseModel model;
    
    // Lista para almacenar animaciones activas
    private List<MedicationAnimation> activeAnimations = new ArrayList<>();
    
    // Para mensajes de estado
    private String statusMessage = null;
    private long statusMessageTime = 0;
    private final long MESSAGE_DURATION = 3000; // 3 segundos
    
    // Para efecto de resaltado del gabinete
    private boolean highlightCabinet = false;
    private long highlightStartTime = 0;
    private final long HIGHLIGHT_DURATION = 1000; // 1 segundo

    // Variables originales
    String currentDirectory;
    int viewSize;

    public HouseView(HouseModel model) {
		super(model, "Domestic Care Robot", model.GridSize);
        this.model = model;
        defaultFont = new Font("Arial", Font.BOLD, 14); // change default font
        setVisible(true);
		currentDirectory = Paths.get("").toAbsolutePath().toString();
		viewSize = model.GridSize;
        setSize(viewSize, viewSize/2);
    }

    /** draw application objects */
    @Override
    public void draw(Graphics g, int x, int y, int object) {
        Location lRobot = model.getAgPos(0);
        Location lOwner = model.getAgPos(1);
		//Location lGuest = model.getAgPos(2);
		Location loc  	= new Location(x, y);
		String objPath = currentDirectory;
        //super.drawAgent(g, x, y, Color.white, -1);
		g.setColor(Color.white);
		super.drawEmpty(g, x, y);
        //System.out.println("Directorio actual sigue siendo: " + currentDirectory);
		switch (object) {
		case HouseModel.BED:
 			g.setColor(Color.lightGray);
			if (model.lBed1.equals(loc)) {   
				//objPath = currentDirectory.concat("/doc/doubleBedlt.png");
				//System.out.println("Cargo la imagen: "+objPath);
				drawMultipleScaledImage(g, x, y, "/doc/doubleBedlt.png", 2, 2, 100, 100);
				g.setColor(Color.red);
				super.drawString(g, x, y, defaultFont, " 1 ");
			};
			if (model.lBed2.equals(loc)) {  
				objPath = "/doc/singleBed.png";//currentDirectory.concat("/doc/singleBed.png");
				drawMultipleScaledImage(g, x, y, objPath, 2, 2, 60, 90); 
				g.setColor(Color.red);
				super.drawString(g, x, y, defaultFont, " 2 ");
			};
			if (model.lBed3.equals(loc)) {
				objPath = "/doc/singleBed.png";//currentDirectory.concat("/doc/singleBed.png");
				drawMultipleScaledImage(g, x, y, objPath, 2, 2, 60, 90); 
				g.setColor(Color.red);
				super.drawString(g, x, y, defaultFont, " 3 ");
			};
            break;                                                                                                  
		case HouseModel.CHAIR:
 			g.setColor(Color.lightGray);
			if (model.lChair1.equals(loc)) {              
				objPath = "/doc/chairL.png";//currentDirectory.concat("/doc/chairL.png");
				drawScaledImageMd(g, x, y, objPath,80,80);
				//g.setColor(Color.red);
				//super.drawString(g, x, y, defaultFont, " 1 ");
			};
			if (model.lChair2.equals(loc)) {  
				objPath = "/doc/chairD.png";//currentDirectory.concat("/doc/chairD.png");
				drawScaledImageMd(g, x, y, objPath,80,80); 
				//g.setColor(Color.red);
				//super.drawString(g, x, y, defaultFont, " 2 ");
			};
			if (model.lChair4.equals(loc)) {  
				objPath = "/doc/chairD.png";//currentDirectory.concat("/doc/chairD.png");
				drawScaledImageMd(g, x, y, objPath,80,80); 
				//g.setColor(Color.red);
				//super.drawString(g, x, y, defaultFont, " 4 ");
			};
			if (model.lChair3.equals(loc)) {
				objPath = "/doc/chairU.png";//currentDirectory.concat("/doc/chairU.png");
				drawScaledImageMd(g, x, y, objPath,80,80);
				//g.setColor(Color.red);
				//super.drawString(g, x, y, defaultFont, " 3 ");
			};
            break;                                                                                                  
		case HouseModel.SOFA:                                                                                      
            g.setColor(Color.lightGray);
			objPath = "/doc/sofa.png";//currentDirectory.concat("/doc/sofa.png");
			drawMultipleScaledImage(g, x, y, objPath, 2, 1, 90, 90);
			//drawMultipleImage(g, x, y, "doc/sofa.png", 2, 1);
            break; 
		case HouseModel.TABLE:
            g.setColor(Color.lightGray);
			objPath = "/doc/table.png";//currentDirectory.concat("/doc/table.png");
			drawMultipleScaledImage(g, x, y, objPath, 2, 1, 80, 80);
            //drawMultipleImage(g, x, y, "doc/table.png", 2, 1);
            break;              
		case HouseModel.DOOR:
			g.setColor(Color.lightGray);
			if (lRobot.equals(loc) | lRobot.isNeigbour(loc) | 
			lOwner.equals(loc) | lOwner.isNeigbour(loc)) {// | 
				//lGuest.equals(loc) | lGuest.isNeigbour(loc)) {
				objPath = "/doc/openDoor2.png";//currentDirectory.concat("/doc/openDoor2.png");
				drawScaledImage(g, x, y, objPath, 75, 100);
                //super.drawAgent(g, x, y, Color.red, -1);
            } else {   
				objPath = "/doc/closeDoor2.png";//currentDirectory.concat("/doc/closeDoor2.png");
				drawScaledImage(g, x, y, objPath, 75, 100);				
			}           
            break;
		case HouseModel.WASHER:
			g.setColor(Color.lightGray);
			if (lRobot.equals(model.lWasher)) {
				objPath = "/doc/openWasher.png";//currentDirectory.concat("/doc/openWasher.png");
				drawScaledImage(g, x, y, objPath, 50, 60);
                //super.drawAgent(g, x, y, Color.red, -1);
            } else {
				objPath = "/doc/closeWasher.png";//currentDirectory.concat("/doc/closeWasher.png");
				drawImage(g, x, y, objPath);
				//drawScaledImage(g, x, y, "doc/closeWasher.png", 50, 60);				
			}           
            break;
        case HouseModel.FRIDGE:
            g.setColor(Color.lightGray); 
			if (lRobot.isNeigbour(model.lFridge)) { 
				objPath = "/doc/openNevera.png";
				drawImage(g, x, y, objPath);
				g.setColor(Color.yellow);
            } else {   
				objPath = "/doc/closeNevera.png";
				drawImage(g, x, y, objPath);	
				g.setColor(Color.blue);
			}                      
            drawString(g, x, y, defaultFont, "Fr ("+model.availableDrugs+")");
            break; 
		case HouseModel.MEDCABINET:
            drawMedCabinet(g, x, y);
            break;
		}
       // repaint();
    }
                          
    @Override
    public void drawAgent(Graphics g, int x, int y, Color c, int id) {
        Location lRobot = model.getAgPos(0);
        Location lOwner = model.getAgPos(1);
		//Location lGuest = model.getAgPos(2);
		String objPath = currentDirectory;

		if (id < 1) { 
			if (!lRobot.equals(lOwner) && !lRobot.equals(model.lFridge)) {
				c = Color.yellow;
				if (model.carryingDrug) {//c = Color.orange;
					//super.drawAgent(g, x, y, c, -1);
					objPath = "/doc/beerBot.png";//currentDirectory.concat("/doc/beerBot.png");
					drawImage(g,x,y,objPath);
				} else {
					objPath = "/doc/bot.png";//currentDirectory.concat("/doc/bot.png");
					drawImage(g,x,y,objPath);
				};
				g.setColor(Color.black);
				super.drawString(g, x, y, defaultFont, "Rob");
			}
		} else if (id > 1) {  
		    drawMan(g, x, y, "down"); 
		} else { 
			if (lOwner.equals(model.lChair1)) {
				drawMan(g, model.lChair1.x, model.lChair1.y, "left"); 
			} else if (lOwner.equals(model.lChair2)) {
				drawMan(g, model.lChair2.x, model.lChair2.y, "down");
			} else if (lOwner.equals(model.lChair4)) {
				drawMan(g, model.lChair4.x, model.lChair4.y, "down");
			} else if (lOwner.equals(model.lChair3)) {      
				drawMan(g, model.lChair3.x, model.lChair3.y, "right");    
			} else if (lOwner.equals(model.lSofa)) {
				drawMan(g, model.lSofa.x, model.lSofa.y, "up");    
			} else if (lOwner.equals(model.lDeliver)) {
				g.setColor(Color.lightGray); 
				objPath = "/doc/openDoor2.png";//currentDirectory.concat("/doc/openDoor2.png");
				drawScaledImage(g, x, y, objPath, 75, 100);
				drawMan(g, x, y, "down");
			} else {
				drawMan(g, x, y, "walkr");         
			};
			if (lRobot.isNeigbour(lOwner)) {	
				String o = "S";
				if (model.sipCount > 0) {
					o +=  " ("+model.sipCount+")";
				}
				g.setColor(Color.yellow);
				drawString(g, x, y, defaultFont, o);
			}                                                           
		}			        
    } 
	
    public void drawMultipleObstacleH(Graphics g, int x, int y, int NCells) {
		for (int i = x; i < x+NCells; i++) {
                drawObstacle(g,i,y); 
            }    
	}

    public void drawMultipleObstacleV(Graphics g, int x, int y, int NCells) {
		for (int j = y; j < y+NCells; j++) {
                drawObstacle(g,x,j);
            }    
    }

    public void drawMultipleImage(Graphics g, int x, int y, String imageAddress, int NW, int NH) {
		URL url = getClass().getResource(imageAddress);
		ImageIcon Img = new ImageIcon();
		if (url == null)
    		System.out.println( "Could not find image! "+imageAddress);
		else Img = new ImageIcon(getClass().getResource(imageAddress)); 
		g.setColor(Color.lightGray);
		g.drawImage(Img.getImage(), x * cellSizeW + 2, y * cellSizeH + 2, NW*cellSizeW - 4, NH*cellSizeH - 4, null);
    }

    public void drawMultipleScaledImage(Graphics g, int x, int y, String imageAddress, int NW, int NH, int scaleW, int scaleH) {
		URL url = getClass().getResource(imageAddress);
		ImageIcon Img = new ImageIcon();
		if (url == null)
    		System.out.println( "Could not find image! "+imageAddress);
		else Img = new ImageIcon(getClass().getResource(imageAddress)); 
		//ImageIcon Img = new ImageIcon(getClass().getResource(imageAddress)); 
		g.setColor(Color.lightGray);
		g.drawImage(Img.getImage(), x * cellSizeW + NW*cellSizeW*(100-scaleW)/200, y * cellSizeH + NH*cellSizeH*(100-scaleH)/200 + 1, NW*cellSizeW*scaleW/100, NH*scaleH*cellSizeH/100, null);
    }
	
    public void drawScaledImage(Graphics g, int x, int y, String imageAddress, int scaleW, int scaleH) {
		URL url = getClass().getResource(imageAddress);
		ImageIcon Img = new ImageIcon();
		if (url == null)
    		System.out.println( "Could not find image!"+imageAddress);
		else Img = new ImageIcon(getClass().getResource(imageAddress)); 
		//ImageIcon Img = new ImageIcon(getClass().getResource(imageAddress)); 
		g.setColor(Color.lightGray);
		g.drawImage(Img.getImage(), x * cellSizeW + cellSizeW*(100-scaleW)/200, y * cellSizeH + cellSizeH*(100-scaleH)/100, cellSizeW*scaleW/100, scaleH*cellSizeH/100, null);
    }

    public void drawScaledImageUp(Graphics g, int x, int y, String imageAddress, int scaleW, int scaleH) {
		URL url = getClass().getResource(imageAddress);
		ImageIcon Img = new ImageIcon();
		if (url == null)
    		System.out.println( "Could not find image! "+imageAddress);
		else Img = new ImageIcon(getClass().getResource(imageAddress)); 
		//ImageIcon Img = new ImageIcon(getClass().getResource(imageAddress));
		g.setColor(Color.lightGray);
		g.drawImage(Img.getImage(), x * cellSizeW + cellSizeW*(100-scaleW)/200, y * cellSizeH + 2, cellSizeW*scaleW/100, scaleH*cellSizeH/100, null);
    }
	
    public void drawScaledImageLf(Graphics g, int x, int y, String imageAddress, int scaleW, int scaleH) {
		URL url = getClass().getResource(imageAddress);
		ImageIcon Img = new ImageIcon();
		if (url == null)
    		System.out.println( "Could not find image! "+imageAddress);
		else Img = new ImageIcon(getClass().getResource(imageAddress)); 
		//ImageIcon Img = new ImageIcon(getClass().getResource(imageAddress));  
		g.setColor(Color.lightGray);
		g.drawImage(Img.getImage(), x * cellSizeW, y * cellSizeH + cellSizeH*(100-scaleH)/200 + 1, cellSizeW*scaleW/100, scaleH*cellSizeH/100, null);
    }
	
    public void drawScaledImageRt(Graphics g, int x, int y, String imageAddress, int scaleW, int scaleH) {
		URL url = getClass().getResource(imageAddress);
		ImageIcon Img = new ImageIcon();
		if (url == null)
    		System.out.println( "Could not find image! "+imageAddress);
		else Img = new ImageIcon(getClass().getResource(imageAddress)); 
		//ImageIcon Img = new ImageIcon(getClass().getResource(imageAddress)); 
		g.setColor(Color.lightGray);
		g.drawImage(Img.getImage(), x * cellSizeW + cellSizeW*(100-scaleW)/100, y * cellSizeH + cellSizeH*(100-scaleH)/200 + 1, cellSizeW*scaleW/100, scaleH*cellSizeH/100, null);
    }
	
    public void drawScaledImageMd(Graphics g, int x, int y, String imageAddress, int scaleW, int scaleH) {
		URL url = getClass().getResource(imageAddress);
		ImageIcon Img = new ImageIcon();
		if (url == null)
    		System.out.println( "Could not find image! "+imageAddress);
		else Img = new ImageIcon(getClass().getResource(imageAddress)); 
		//ImageIcon Img = new ImageIcon(getClass().getResource(imageAddress)); 
		g.setColor(Color.lightGray);
		g.drawImage(Img.getImage(), x * cellSizeW + cellSizeW*(100-scaleW)/200, y * cellSizeH + cellSizeH*(100-scaleH)/200 + 1, cellSizeW*scaleW/100, scaleH*cellSizeH/100, null);
    }

    public void drawImage(Graphics g, int x, int y, String imageAddress) {
        URL url = getClass().getResource(imageAddress);
        ImageIcon Img = new ImageIcon();
        if (url == null)
            System.out.println("Could not find image! " + imageAddress);
        else
            Img = new ImageIcon(getClass().getResource(imageAddress));
        // g.setColor(Color.lightGray);
        g.drawImage(Img.getImage(), x * cellSizeW + 1, y * cellSizeH + 1, cellSizeW - 2, cellSizeH - 2, null);
    }
	
    public void drawMan(Graphics g, int x, int y, String how) { 
		String resource = "/doc/sitd.png";//currentDirectory.concat("/doc/sitd.png");
		switch (how) {
			case "right": resource = "/doc/sitr.png";//currentDirectory.concat("/doc/sitr.png"); 
			break;
			case "left": resource = "/doc/sitl.png";//currentDirectory.concat("/doc/sitl.png");  
			break;     
			case "up": resource = "/doc/situ.png";//currentDirectory.concat("/doc/situ.png");  
			break;     
			case "down": resource = "/doc/sitd.png";//currentDirectory.concat("/doc/sitd.png"); 
			break;
			case "stand": resource = "/doc/sits.png";//currentDirectory.concat("/doc/sits.png"); 
			break;
			case "walkr": resource = "/doc/walklr.png";//currentDirectory.concat("/doc/walklr.png"); 
			break;
        }
		URL url = getClass().getResource(resource);
		ImageIcon Img = new ImageIcon();
		if (url == null)
    		System.out.println( "Could not find image! "+resource);
		else Img = new ImageIcon(getClass().getResource(resource)); 
		//ImageIcon Img = new ImageIcon(getClass().getResource(resource));
		g.drawImage(Img.getImage(), x * cellSizeW + 1, y * cellSizeH + 1, cellSizeW - 3, cellSizeH - 3, null);
    }
        
    public void drawManSittingRight(Graphics g, int x, int y) {
		String objPath = "/doc/sitr.png";//currentDirectory.concat("/doc/sitr.png");
		URL url = getClass().getResource(objPath);
		ImageIcon Img = new ImageIcon();
		if (url == null)
    		System.out.println( "Could not find image! "+objPath);
		else Img = new ImageIcon(getClass().getResource(objPath)); 
		//ImageIcon Img = new ImageIcon(getClass().getResource(objPath));
		g.drawImage(Img.getImage(), x * cellSizeW - 4, y * cellSizeH + 1, cellSizeW + 2, cellSizeH - 2, null);
    }
        
    public void drawSquare(Graphics g, int x, int y) {
        g.setColor(Color.blue);
        g.drawRect(x * cellSizeW + 2, y * cellSizeH + 2, cellSizeW - 4, cellSizeH - 4);
        g.setColor(Color.cyan);
        g.drawRect(x * cellSizeW + 1, y * cellSizeH + 1, cellSizeW - 3, cellSizeH - 3);   
    }   
	
    private void drawMedCabinet(Graphics g, int x, int y) {
        // Obtener las posiciones de los agentes
        Location lRobot = model.getAgPos(0);
        Location lOwner = model.getAgPos(1);
        
        // Verificar si está abierto o si algún agente está cerca
        if (model.medCabinetOpen || 
            lRobot.distance(model.lMedCabinet) == 1 || 
            lOwner.distance(model.lMedCabinet) == 1) {
            
            // Dibujar gabinete abierto
            String objPath = "/doc/medicinas_abierto.jpeg";
            drawImage(g, x, y, objPath);
            g.setColor(Color.yellow);
            
            // Mostrar información básica de medicamentos si está abierto
            if (model.medCabinetOpen) {
                drawMedicationIcons(g, x, y);
            }
        } else {
            // Dibujar gabinete cerrado
            String objPath = "/doc/medicinas_cerrado.jpeg";
            drawImage(g, x, y, objPath);
            g.setColor(Color.blue);
            drawString(g, x, y, defaultFont, "Gabinete");
        }
    }
    
    // Dibuja iconos representando los medicamentos disponibles
    void drawMedicationIcons(Graphics g, int x, int y) {
        // Obtener nombres de medicamentos
        java.util.Set<String> medNames = model.getMedicationNames();
        
        if (medNames != null && !medNames.isEmpty()) {
            StringBuilder info = new StringBuilder("Meds: ");
            int count = 0;
            
            for (String medName : medNames) {
                // Limitar a 3 medicamentos por línea
                if (count >= 3) break;
                
                // Obtener información del medicamento
                HouseModel.Medication med = model.getMedicationInfo(medName);
                if (med != null) {
                    String shortName = medName.substring(0, Math.min(2, medName.length())).toUpperCase();
                    info.append(shortName).append("(").append(med.quantity).append(") ");
                    count++;
                }
            }
            
            drawString(g, x, y, defaultFont, info.toString());
        }
    }
    
    // Genera un color único para cada medicamento
    Color getMedicationColor(String medName) {
        if (medName == null) return Color.WHITE;
        
        // Generar un color basado en el nombre del medicamento
        int hash = medName.hashCode();
        int r = (hash & 0xFF0000) >> 16;
        int g = (hash & 0x00FF00) >> 8;
        int b = hash & 0x0000FF;
        
        // Asegurar que el color sea visible
        r = Math.max(r, 100);
        g = Math.max(g, 100);
        b = Math.max(b, 100);
        
        return new Color(r, g, b);
    }
    
    /**
     * Clase para las animaciones de los medicamentos
     */
    class MedicationAnimation {
        String medName;
        int startX, startY;
        int currentX, currentY;
        int targetX, targetY;
        int frames = 20; // duración de la animación
        int currentFrame = 0;
        Color color;
        
        MedicationAnimation(String medName, int startX, int startY, int targetX, int targetY) {
            this.medName = medName;
            this.startX = this.currentX = startX;
            this.startY = this.currentY = startY;
            this.targetX = targetX;
            this.targetY = targetY;
            this.color = getMedicationColor(medName);
        }
        
        boolean update() {
            currentFrame++;
            
            if (currentFrame <= frames) {
                // Actualizar posición interpolando
                float progress = (float)currentFrame / frames;
                currentX = startX + (int)((targetX - startX) * progress);
                currentY = startY + (int)((targetY - startY) * progress);
                return true;
            }
            
            return false; // La animación ha terminado
        }
        
        void draw(Graphics g) {
            int size = 10 + (int)(5 * (1 - (float)currentFrame / frames));
            g.setColor(color);
            g.fillOval(currentX - size/2, currentY - size/2, size, size);
            
            // Dibujar nombre del medicamento
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 10));
            g.drawString(medName, currentX - size, currentY - size);
        }
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        // Comprobar si ha cambiado el estado del gabinete
        if (model.getMedCabinetStateChanged()) {
            highlightCabinet = true;
            highlightStartTime = System.currentTimeMillis();
        }
        
        // Comprobar si se ha tomado un medicamento recientemente
        String takenMed = model.getAndResetLastMedicationTaken();
        if (takenMed != null) {
            // Calcular posiciones para la animación (desde el gabinete hasta el agente que lo tomó)
            int startX = model.lMedCabinet.x * cellSizeW + cellSizeW/2;
            int startY = model.lMedCabinet.y * cellSizeH + cellSizeH/2;
            
            // Asumimos que el último agente en actuar fue quien tomó el medicamento
            Location targetLoc = model.getLastAgentToAct() == 0 ? model.getAgPos(0) : model.getAgPos(1);
            int targetX = targetLoc.x * cellSizeW + cellSizeW/2;
            int targetY = targetLoc.y * cellSizeH + cellSizeH/2;
            
            // Crear la animación
            activeAnimations.add(new MedicationAnimation(takenMed, startX, startY, targetX, targetY));
            
            // Actualizar mensaje de estado
            statusMessage = "Tomado: " + takenMed + " (Quedan: " + model.getLastMedicationQuantity() + ")";
            statusMessageTime = System.currentTimeMillis();
        }
        
        // Actualizar y dibujar animaciones activas
        Iterator<MedicationAnimation> it = activeAnimations.iterator();
        while (it.hasNext()) {
            MedicationAnimation anim = it.next();
            if (!anim.update()) {
                it.remove(); // Eliminar animaciones terminadas
            } else {
                anim.draw(g);
            }
        }
        
        // Dibujar mensaje de estado si no ha caducado
        long currentTime = System.currentTimeMillis();
        if (statusMessage != null && currentTime - statusMessageTime < MESSAGE_DURATION) {
            g.setColor(new Color(0, 0, 0, 200));
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString(statusMessage, 20, getHeight() - 20);
        }
        
        // Dibujar el gabinete de medicamentos en su posición correcta
        int cabX = model.lMedCabinet.x * cellSizeW;
        int cabY = model.lMedCabinet.y * cellSizeH;
        drawMedCabinet(g, cabX, cabY);
        
        // Solicitar repintado constante si hay animaciones activas
        if (!activeAnimations.isEmpty()) {
            repaint(50); // Repintar cada 50ms para animaciones suaves
        }
    }
}
