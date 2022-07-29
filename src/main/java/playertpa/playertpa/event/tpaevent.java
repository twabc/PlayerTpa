package playertpa.playertpa.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

        @SuppressWarnings("unused")
        public class tpaevent extends Event implements Cancellable {

            private static final HandlerList HANDLERS_LIST = new HandlerList();

            private Player player;
            private org.bukkit.Location locationBeforeTpa;
            private boolean isCancelled;

            public tpaevent(Player player, org.bukkit.Location location) {
                this.player = player;
                this.locationBeforeTpa = location;
            }

            public boolean isCancelled() {
                return isCancelled;
            }

            public void setCancelled(boolean cancelled) {
                this.isCancelled = cancelled;
            }

            public HandlerList getHandlers() {
                return HANDLERS_LIST;
            }

            public static HandlerList getHandlerList() {
                return HANDLERS_LIST;
            }

            public Player getPlayer() {
                return player;
            }

            public void setPlayer(Player player) {
                this.player = player;
            }

            public org.bukkit.Location getLocationBeforeTpa() {
                return locationBeforeTpa;
            }

            public void setLocationBeforeTpa(org.bukkit.Location locationBeforeTpa) {
                this.locationBeforeTpa = locationBeforeTpa;
            }
        }
