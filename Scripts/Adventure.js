var player;
var spawn;
var music;
var particles;

var initialized = false;

function init(){
	music = SoundManager.addMusic("music.wav");
	player = EntityManager.getFromID("player");
	spawn = EntityManager.getFromID("spawn");
	particles = EntityManager.CreateEntity("PARTICLE_EMITTER", "0,0,adventure.txt", "");
}

function tick(){
		if(!initialized){
			initialized = true;
			EntityManager.add(particles);
		}
		particles.moveTo(player.x, player.y);
		if(player.getY() > 10000){
			WorldManager.add("Arcade");
			WorldManager.clearAllRooms();
			SoundManager.removeMusic(music);
			EntityManager.remove(particles);
		}
}

function render(g){

}
