var particleEmitter;

function init(){
	out.println("Test Running");
}

function tick(){
	if(Mouse.IsPressed&&Mouse.IsRightClick){
		EntityManager.add(EntityManager.CreateEntity("TORCH", MouseLocation.x+","+MouseLocation.y+",id",""));
	}
}

function render(g){

}