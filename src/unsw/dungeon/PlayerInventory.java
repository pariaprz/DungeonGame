package unsw.dungeon;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class PlayerInventory {
    private final ListProperty<Pair<Collectable, Integer>> items;
    public static final int UNEQUIPPED = -1;
    private final Player player;
    private final IntegerProperty equippedIndex = new SimpleIntegerProperty(UNEQUIPPED);
    private final IntegerProperty usableIndex = new SimpleIntegerProperty(equippedIndex.get());


    public PlayerInventory(Player player) {
        ObservableList<Pair<Collectable, Integer>> observableList = FXCollections.observableArrayList(new ArrayList<>());
        items = new SimpleListProperty<>(observableList);
        this.player = player;
        player.status().addListener((observableValue, old, newVal) -> {
            if (newVal.equals(DefaultPlayerState.STATE_NAME)) {
                if (equippedIndex.get() != UNEQUIPPED) {
                    player.setStatus(getEquipped().getClass().toString());
                }
            }
        });
        equippedIndex.addListener((observableValue, old, newVal) -> {
            if ((int)newVal == UNEQUIPPED) {
                usableIndex.setValue(UNEQUIPPED);
                return;
            }
            int usableCount = 0;
            for (int i = 0; i < equippedIndex.get(); i++) {
                if (items.get(i).getKey().canUse(this)) {
                    usableCount += 1;
                }
            }
            usableIndex.setValue(usableCount);
        });
    }

    public ListProperty<Pair<Collectable, Integer>> getItems() {
        return items;
    }

    public IntegerProperty getUsableIndex() {
        return usableIndex;
    }

    public void addIfNotPresent(Collectable entity) {
        items.stream()
                .filter(pair -> pair.getKey().getClass() == entity.getClass() && pair.getValue() > 0)
                .findFirst()
                .ifPresentOrElse((a) -> {}, () ->
                        items.add(new Pair<>(entity, 1))
                );
        items.removeIf(pair -> pair.getKey().getClass() == entity.getClass() && pair.getValue() <= 0);
        if (isUnequipped()) {
            cycleInventory();
        }
    }

    private void equipFirst() {
        equippedIndex.setValue(IntStream.range(0, items.size())
                .filter(i -> items.get(i).getKey().canUse(this))
                .findFirst()
                .orElse(-1));
        if (!isUnequipped() && player.getPlayerState() instanceof DefaultPlayerState) {
            player.setStatus(getEquipped().getClass().toString());
        } else if (player.getPlayerState() instanceof DefaultPlayerState) {
            player.setStatus(Player.DEFAULT_STATUS);
        }
    }

    public void cycleInventory() {
        if (items.stream().noneMatch(p -> p.getKey().canUse(this))) {
            equippedIndex.setValue(UNEQUIPPED);
            if (player.getPlayerState() instanceof DefaultPlayerState) {
                player.setStatus(Player.DEFAULT_STATUS);
            }
            return;
        }
        if (equippedIndex.get() == UNEQUIPPED || !getEquipped().canUse(this)) {
            equipFirst();
            return;
        }

        int initial = equippedIndex.get();
        int i = initial;
        do {
            i = (i + 1) % items.size();
            if (items.get(i).getKey().canUse(this)) {
                equippedIndex.setValue(i);
                break;
            }
        } while (i != initial);
        if (!isUnequipped() && player.getPlayerState() instanceof DefaultPlayerState) {
            player.setStatus(getEquipped().getClass().toString());
        } else if (player.getPlayerState() instanceof DefaultPlayerState) {
            player.setStatus(Player.DEFAULT_STATUS);
        }
    }

    public boolean isUnequipped() {
        return equippedIndex.get() == UNEQUIPPED;
    }

    public void useEquipped() {
        if (!isUnequipped() && player.getPlayerState() instanceof DefaultPlayerState) {
            items.get(equippedIndex.get()).getKey().use(player);
        }
    }

    public void addOrIncrement(Collectable entity, int num) {
        final boolean[] found = {false};
        items.replaceAll(obj -> {
            if (obj.getKey().getClass() == entity.getClass()) {
                found[0] = true;
                return new Pair<>(obj.getKey(), obj.getValue() + num);
            } else {
                return obj;
            }
        });
        if (!found[0]) {
            items.add(new Pair<>(entity, num));
        }
        if (isUnequipped()) {
            equippedIndex.setValue(UNEQUIPPED);
            cycleInventory();
        }
    }

    public void decrementIfExists(Collectable entity) {
        items.replaceAll(obj -> obj.getKey().getClass() == entity.getClass() ?
                new Pair<>(obj.getKey(), obj.getValue() -1) : obj);
        boolean removed = items.removeIf(p -> p.getValue() <= 0);
        if (removed && player.getPlayerState() instanceof DefaultPlayerState) {
            equippedIndex.setValue(UNEQUIPPED);
            cycleInventory();
        }
    }

    public boolean contains(Collectable entity) {
        return getCount(entity) > 0;
    }

    public Collectable getEquipped() {
        if (equippedIndex.get() == UNEQUIPPED) return null;
        return items.get(equippedIndex.get()).getKey();
    }

    public int getCount(Collectable entity) {
        return items.stream()
                .filter(p -> p.getKey().getClass() == entity.getClass())
                .findFirst()
                .orElse(new Pair<>(null, 0)).getValue();
    }

    public <T extends Collectable> T popItemIfExists(T entity) {
        if (!contains(entity)) return null;
        final int[] indx = {0};
        final int[] removedIndex = {0};
        T result = items.stream()
                .filter(p -> {
                    if (p.getKey().getClass() == entity.getClass()) {
                        removedIndex[0] = indx[0];
                        return true;
                    }
                    indx[0]++;
                    return false;
                })
                .map( e -> (T)e.getKey())
                .findFirst()
                .orElse(null);
        items.removeIf(p -> p.getKey().getClass() == entity.getClass());
        if (result != null) {
            equippedIndex.setValue(UNEQUIPPED);
            cycleInventory();
        }
        return result;
    }
}