/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

import Entity.EnumEntityType;
import Item.ItemStack;
import Item.Items;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Bailey
 * @param <var>
 */
public class Attribute <var>{
    
    private String preface = "";
    private Object dataType;
    
    private int length = 16;
    
    public boolean editable = true;
    
    private boolean wasEdited = false;
    
    public Attribute(String preface, var inData){
        dataType = inData;
        this.preface = preface;
    }
    
    public Attribute setCanEdit(boolean b){
        this.editable = b;
        return this;
    }
    
    public Attribute setLength(int i){
        this.length = i;
        return this;
    }
    
    public void render(int x, int y, Graphics g){
       if(dataType instanceof ItemStack){
           if(((ItemStack)dataType).item != null){
                g.drawString(preface+((ItemStack)dataType).item.getName()+","+((ItemStack)dataType).count, x, y); 
           }
       }else
       g.drawString(preface+dataType+"", x, y); 
    }
    
    public void highlightRender(int x, int y, Graphics g){
       if(dataType instanceof ItemStack){
           if(((ItemStack)dataType).item != null){
                g.setColor(Color.decode("#ededed"));
                g.fillRect(x-(8 * 4), y-(16 * 4), (16 * 4), (16 * 4));
                ((ItemStack)dataType).item.renderInWorld(x, y-(8 * 4), g);
                g.setColor(Color.WHITE);
                g.drawRect(x-(8 * 4), y-(16 * 4), (16 * 4), (16 * 4));
           }
       }
    }
    
    public var getData(){
        return (var)this.dataType;
    }
    
    public void userChangeData(){
        if(!this.editable){
            return;
        }
        
        String input;
        
        if(dataType instanceof ItemStack){
            input = "";
        }else{
            input = JOptionPane.showInputDialog(preface,dataType);
        }
        
        if(input == null){
            return;
        }
        Object preData = this.dataType;
        try{
            if(dataType instanceof Integer){
                this.dataType = (Object)Integer.parseInt(input);
            }else if(dataType instanceof Float){
                this.dataType = (Object)Float.parseFloat(input);
            }else if(dataType instanceof Boolean){
                this.dataType = (Object)Boolean.parseBoolean(input);
            }else if(dataType instanceof String){
                this.dataType = (Object)(input);
            }else if(dataType instanceof String[]){
                JComboBox c = new JComboBox();
                for(int i = 0; i < ((String[])(this.dataType)).length; i++){
                    c.addItem(((String[])(this.dataType))[i]);
                }
                
                JPanel panel = new JPanel();
                panel.add(c);
//                panel.setVisible(true);

                input = JOptionPane.showInputDialog(panel);
                if(Items.valueOf(((String[])(this.dataType))[c.getSelectedIndex()])!=null){
                    
                }
            }else if(dataType instanceof Byte){
                this.dataType = (Object)Byte.parseByte(input);
            }else if(dataType instanceof EnumEntityType){
                this.dataType = (Object)EnumEntityType.valueOf(input);
            }else if(dataType instanceof ItemStack){
                //All of the items that exis as an array
                String[] choices = new String[Items.values().length+1];
                choices[0]="NULL";
                for(int i = 0; i < Items.values().length; i++){
                    choices[i+1] = Items.values()[i].name();
                }

                JComboBox c = new JComboBox();
                for(int i = 0; i < choices.length; i++){
                    c.addItem(choices[i]);
                }
                
                SpinnerNumberModel sModel = new SpinnerNumberModel(0, 0, 30, 1);
                JSpinner spinner = new JSpinner(sModel);
                
                JPanel panel = new JPanel();
                panel.add(c);
                panel.add(spinner);
//                panel.setVisible(true);

                input = JOptionPane.showInputDialog(panel);
                int value = (Integer) spinner.getValue();
                
                System.out.println(input+":"+choices[c.getSelectedIndex()]+":"+value);
                if(Items.valueOf(choices[c.getSelectedIndex()])!=null){
                    this.dataType = new ItemStack(Items.valueOf(choices[c.getSelectedIndex()]).generate(input.split(",")), value);
                }else{
                    this.dataType = null;
                }
            }else{
                System.out.println("Data Type not recognised:"+dataType.getClass().getName());
            }
        }catch(Exception e){
            this.dataType = preData;
        }
        wasEdited = true;
    }
    
    public boolean wasEdited(){
        boolean out = this.wasEdited;
        this.wasEdited = false;
        return out;
    }
    
    public void setData(var i){
        this.dataType = i;
    }
    
    public void setDataFromString(String i){
        String input = i;
        if(input == null){
            return;
        }
        Object preData = this.dataType;
        try{
            if(dataType instanceof Integer){
                this.dataType = (Object)Integer.parseInt(input);
            }else if(dataType instanceof Float){
                this.dataType = (Object)Float.parseFloat(input);
            }else if(dataType instanceof Boolean){
                this.dataType = (Object)Boolean.parseBoolean(input);
            }else if(dataType instanceof String){
                this.dataType = (Object)(input);
            }else if(dataType instanceof Byte){
                this.dataType = (Object)Byte.parseByte(input);
            }else if(dataType instanceof EnumEntityType){
                this.dataType = (Object)EnumEntityType.valueOf(input);
            }else{
                System.out.println("Data Type not recognised:"+dataType.getClass().getName());
            }
        }catch(Exception e){
            this.dataType = preData;
        }
    }
}
