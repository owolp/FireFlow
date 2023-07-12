#!/usr/bin/env bash
#
# Copyright (C) 2023 Zitech Ltd.
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

# This gets the list of staged files that will be included in the commit (removed files are ignored).
# If a file is only partially staged (some changes in it are unstaged), then the file is not included
# in checks. That is because the checks re-add the file to ensure autocorrected fixes are included
# in the commit, and we don't want to add the whole file since that would mess with the intention
# of partial staging.
stagedFilename=.tmpStagedFiles
unstagedFilename=.tmpUnstagedFiles

git diff --cached --name-only --diff-filter=ACM | grep '\.kt[s"]\?$' | sort > $stagedFilename
git diff --name-only --diff-filter=ACM | grep '\.kt[s"]\?$' | sort > $unstagedFilename

# This returns a list of lines that are only in the staged list
filesToCheck=$(comm -23 $stagedFilename $unstagedFilename | tr '\n' ' ')

# Delete the files, which were created for temporary processing
rm -f $stagedFilename
rm -f $unstagedFilename

# If the list of files is not empty then run checks on them
if [ ! -z "$filesToCheck" ]
then
	ktlint --format --code-style=android_studio $filesToCheck
	if [ $? -ne 0 ]; then exit 1; fi
	# If we haven't exited because of an error it means we were able to
	# fix issues with autocorrect. However, we need to add the changes in
	# order for them to be included in the pending commit.
	git add $filesToCheck
fi