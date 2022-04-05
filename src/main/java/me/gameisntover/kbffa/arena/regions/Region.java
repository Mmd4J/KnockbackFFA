package me.gameisntover.kbffa.arena.regions;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Data
public class Region implements ConfigurationSerializable {
    private double x1;
    private double y1;
    private double z1;
    private double x2;
    private double y2;
    private double z2;
    private World world;
    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String,Object> map = new HashMap();
        map.put("x1",x1);
        map.put("x2",x2);
        map.put("y1",y1);
        map.put("y2",y2);
        map.put("z1",z1);
        map.put("z2",z2);
        map.put("world",world);
        return map;
    }
    public Region(Location loc1,Location loc2){
        x1 = loc1.getX();
        y1 = loc1.getY();
        z1 = loc1.getZ();
        x2 = loc2.getX();
        y2 = loc2.getY();
        z2 = loc2.getZ();
        world = loc1.getWorld();
    }
    public Region(int x1,int y1, int z1, int x2, int y2, int z2, World world){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.z1 = z1;
        this.z2 = z2;
        this.world = world;
    }
    public Location getPosition1(){
        return new Location(world,x1,y1,z1);
    }
    public Location getPosition2(){
        return new Location(world,x2,y2,z2);
    }
    public boolean contains(Location location){
        return (location.getX() >= x1 && location.getX() <= x2 && location.getY() >= y1 && location.getY() <= y2 && location.getZ() >= z1 && location.getZ() <= z2 && location.getWorld() == world);
    }
    }
