/*
 * Copyright (C) 2023 Zitech Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import dev.zitech.fireflow.ds.molecules.dialog.DialogRadioItem;

public class DialogRadioItemBuilder {

    private Integer id = 0;
    private String text = "Text";
    private Boolean selected = false;
    private Boolean enabled = false;

    public DialogRadioItemBuilder setId(Integer id) {
        this.id = id;
        return this;
    }

    public DialogRadioItemBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public DialogRadioItemBuilder setSelected(Boolean selected) {
        this.selected = selected;
        return this;
    }

    public DialogRadioItemBuilder setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public DialogRadioItem build() {
        return new DialogRadioItem(
                id,
                text,
                selected,
                enabled
        );
    }
}
