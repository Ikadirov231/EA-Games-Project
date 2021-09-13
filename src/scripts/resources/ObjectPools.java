package scripts.resources;

import java.util.ArrayList;
import java.util.List;

public class ObjectPools {
    private List<GameObject> structurePool; // Object pool for interactable structures
    private List<GameObject> monsterPool; // Object pool for monsters
    private List<GameObject> entityPool; // Object pool for entities (e.g. shopkeeper)
    private List<GameObject> effectsPool; // Object pool for effects (e.g. bullets)
    private Player player;

    public ObjectPools(Player player) {
        structurePool = new ArrayList<GameObject>();
        monsterPool = new ArrayList<GameObject>();
        entityPool = new ArrayList<GameObject>();
        effectsPool = new ArrayList<GameObject>();
        this.player = player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean hasPlayer() {
        return this.player != null;
    }

    public Player getPlayer() {
        return player;
    }

    public void setStructurePool(ArrayList<GameObject> structurePool) {
        this.structurePool = structurePool;
    }

    public void setMonsterPool(ArrayList<GameObject> monsterPool) {
        this.monsterPool = monsterPool;
    }

    public void setEntityPool(ArrayList<GameObject> entityPool) {
        this.entityPool = entityPool;
    }

    public void setEffectsPool(ArrayList<GameObject> effectsPool) {
        this.effectsPool = effectsPool;
    }

    public ArrayList<GameObject> getStructurePool() {
        return (ArrayList<GameObject>) structurePool;
    }

    public ArrayList<GameObject> getMonsterPool() {
        return (ArrayList<GameObject>) monsterPool;
    }

    public ArrayList<GameObject> getEntityPool() {
        return (ArrayList<GameObject>) entityPool;
    }

    public ArrayList<GameObject> getEffectsPool() {
        return (ArrayList<GameObject>) effectsPool;
    }

    public void clearStructurePool() {
        structurePool = new ArrayList<GameObject>();
    }
}
