package org.fife.ui.rsyntaxtextarea.modes.antlr;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.misc.IntegerStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Maps a stack of {@link ModeInfo}s to a single int.
 *
 * @author Markus Heberling
 */
class ModeInfoManager {
	private int nextModeInternalToken = -1;

	private final Map<Integer, ModeInfo> tokenToModeInfo = new HashMap<>();
	private final Map<ModeInfo, Integer> modeInfoToToken = new HashMap<>();

	static final class ModeInfo {
		final int tokenType;
		final int currentMode;
		final IntegerStack modeStack;

		ModeInfo(int tokenType, int currentMode, IntegerStack modeStack) {
			this.tokenType = tokenType;
			this.currentMode = currentMode;
			this.modeStack = modeStack;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			} else if (o == null || getClass() != o.getClass()) {
				return false;
			} else {
				ModeInfo modeInfo = (ModeInfo) o;
				return tokenType == modeInfo.tokenType &&
					currentMode == modeInfo.currentMode &&
					modeStack.equals(modeInfo.modeStack);
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(tokenType, currentMode, modeStack);
		}
	}

	ModeInfo getModeInfo(int initialTokenType) {
		ModeInfo modeInfo;
		if (initialTokenType < 0) {
			// extract modes
			modeInfo = tokenToModeInfo.get(initialTokenType);
		} else {
			modeInfo = new ModeInfo(initialTokenType, Lexer.DEFAULT_MODE, new IntegerStack());
		}
		return modeInfo;
	}

	int storeModeInfo(int currentType, int currentMode, IntegerStack modeStack) {
		ModeInfo modeInfo = new ModeInfo(currentType, currentMode, new IntegerStack(modeStack));
		Integer token = modeInfoToToken.get(modeInfo);
		if (token != null) {
			return token;
		} else {
			if (nextModeInternalToken > 0) {
				// overflow, we can't store anymore variations of ModeInfos
				throw new ArrayIndexOutOfBoundsException(nextModeInternalToken);
			}
			tokenToModeInfo.put(nextModeInternalToken, modeInfo);
			modeInfoToToken.put(modeInfo, nextModeInternalToken);
			return nextModeInternalToken--;
		}
	}
}
