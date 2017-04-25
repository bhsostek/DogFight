/**
* Created by Bailey on 12/8/2016.
*/
var rift;
var sprites = [SpriteBinder.loadSprite("rift_0.png"),
              SpriteBinder.loadSprite("rift_1.png"),
              SpriteBinder.loadSprite("rift_2.png"),
              SpriteBinder.loadSprite("rift_3.png"),
              SpriteBinder.loadSprite("rift_4.png"),
              SpriteBinder.loadSprite("rift_5.png"),
              SpriteBinder.loadSprite("rift_6.png"),
              SpriteBinder.loadSprite("rift_7.png"),
              SpriteBinder.loadSprite("rift_8.png"),
              SpriteBinder.loadSprite("rift_9.png"),
              SpriteBinder.loadSprite("rift_10.png"),
              SpriteBinder.loadSprite("rift_11.png")];

var x = 1600;
var y = 580;

var count = 0;
var maxCount = 50;

var script;

function init(script){
    out.println("Rift Loaded");
    this.script = script;
}

function tick(){
    if(EntityManager.getFromID("id:0.8641319770275622").getNodes()[0].isPowered()){
        var pedestal = EntityManager.getFromID("ItemPedestal");
        var pedestal_item = pedestal.getItem();
        if(pedestal_item.item != null){
            if(pedestal_item.item.instanceOf("PAPER")) {
                if (count < maxCount) {
                    count++;
                    if (count == maxCount) {
                        var rift = EntityManager.CreateEntity("WARP", (x + "," + y + ",5,5,"+pedestal_item.item.getDestination()+",false,0,0,false,rift"), "");
                        EntityManager.add(rift);
                    }
                }
            }
        }
    }

    if(Keyboard.getKey("R")){
        if(player.takeItem(Items.get("BERRY").generate(""),4)){
            player.addItem(Items.get("PETUNIA").generate(""),1);
        }
    }
}

function render(g) {
    var sprite = sprites[parseInt((count / maxCount) * sprites.length)-1];
    if(sprite!=null) {
        sprite.render(x, y, (sprite.width * 4), (sprite.height * 4), g);
    }
}
