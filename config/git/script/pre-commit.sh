#!/usr/bin/env bash
#
# Copyright (C) 2022 Zitech
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.
#

OUTPUT="/tmp/ktlint-$(date +%s)"
git diff --name-only --cached --relative | grep '\.kt[s"]\?$' | grep -v "Test.kt" | xargs ktlint --android --relative . > "$OUTPUT"
EXIT_CODE=$?
if [ $EXIT_CODE -ne 0 ]; then
  echo "***************************************************"
  echo "                   Ktlint failed                   "
  echo " Please fix the following issues before committing "
  echo "***************************************************"
  cat "$OUTPUT"
  exit $EXIT_CODE
fi