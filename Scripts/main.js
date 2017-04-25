var ticks = 0;
var frames = 0;
var count = 0;

var sprite = SpriteBinder.loadSprite("entity/bookshelf/bookshelf_full.png");

function init(){
	out.println("Hello World");
}

function tick(){
	ticks++;
	if(ticks>=60){
		ticks=0;
		out.println("Frames:"+frames);
		frames = 0;
	}
	if(Keyboard.getKey("SPACE")){
		out.println("Space Is Pressed");
		//var entity = entityManager.CreateEntity("BARREL",count+","+count);
		entityManager.add(entity);
		count+=10;
	}
}

function render(g){
	frames++;
	sprite.render(100,100,g);
}