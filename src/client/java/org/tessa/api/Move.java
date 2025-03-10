package org.tessa.api;
import net.minecraft.world.entity.player.Input;
public enum Move {

    FORWARD(new Input(true, false, false, false, false, false, false )),
    FORWARD_LEFT(new Input(true, false, true, false, false, false, false )),
    FORWARD_RIGHT(new Input(true, false, false, true, false, false, false )),
    BACKWARD(new Input(false, true, false, false, false, false, false )),
    BACKWARD_LEFT(new Input(false, true, true, false, false, false, false)),
    BACKWARD_RIGHT(new Input(false, true, false, true, false, false, false )),
    LEFT(new Input(false, false, true, false, false, false, false )),
    RIGHT(new Input(false, false, false, true, false, false, false )),
    SPRINT(new Input(true, false, false, false, false, false, true )),
    CROUCH(new Input(false, false, false, false, false, true, false )),
    STAND_TALL(new Input(false, false, false, false, false, false, false )),
    JUMP(new Input(false, false, false, false, true, false, false )),
    JUMP_FORWARD(new Input(true, false, false, false, true, false, false )),
    JUMP_FORWARD_LEFT(new Input(true, false, true, false, true, false, false )),
    JUMP_FORWARD_RIGHT(new Input(true, false, false, true, true, false, false )),
    JUMP_BACKWARD(new Input(false, true, false, false, true, false, false )),
    JUMP_BACKWARD_LEFT(new Input(false, true, true, false, true, false, false)),
    JUMP_BACKWARD_RIGHT(new Input(false, true, false, true, true, false, false )),
    JUMP_LEFT(new Input(false, false, true, false, true, false, false )),
    JUMP_RIGHT(new Input(false, false, false, true, true, false, false ));

    private final Input move;
    Move(Input move) {
        this.move = move;
    }

    public Input move() {
        return move;
    }

}
