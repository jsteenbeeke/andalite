package com.jeroensteenbeeke.andalite.java.util;

import com.github.javaparser.Position;
import com.github.javaparser.ast.Node;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.lux.TypedResult;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Locations
{

	public static final int UNICODE_MARKER_TWO_BYTE = 0b110;

	public static final int UNICODE_MARKER_THREE_BYTE = 0b1110;

	public static final int UNICODE_MARKER_FOUR_BYTE = 0b11110;

	public static final int UNICODE_MARKER_CONTINUATION = 0b10;

	public static Location from(Node node, FileMap indexes)
	{
		return node.getRange()
			.map(r -> new Location(indexes.get(r.begin), indexes.get(r.end)))
			.orElseThrow(() -> new IllegalArgumentException(
				"Cannot transform node without range to location"));
	}

	public static TypedResult<FileMap> indexFile(File targetFile)
	{
		FileMap positions = new FileMap();
		int line = 1;
		int p = 1;

		try (PushbackInputStream fis = new PushbackInputStream(new FileInputStream(targetFile), 3))
		{
			int b;
			int column = 1;

			while ((b = fis.read()) != -1)
			{
				if (b >> 5 == UNICODE_MARKER_TWO_BYTE)
				{
					// 2-byte unicode character
					int b2 = fis.read();

					if (b2 >> 6 != UNICODE_MARKER_CONTINUATION)
					{
						// malformed unicode
						fis.unread(b2);
					}
				}
				else if (b >> 4 == UNICODE_MARKER_THREE_BYTE)
				{
					// 3-byte unicode character
					int b2 = fis.read();
					int b3 = fis.read();
					if (b2 >> 6 != UNICODE_MARKER_CONTINUATION
						|| b3 >> 6 != UNICODE_MARKER_CONTINUATION)
					{
						fis.unread(b3);
						fis.unread(b2);
					}
				}
				else if (b >> 3 == UNICODE_MARKER_FOUR_BYTE)
				{
					// 4 character sequence
					int b2 = fis.read();
					int b3 = fis.read();
					int b4 = fis.read();
					if (b2 >> 6 != UNICODE_MARKER_CONTINUATION
						|| b3 >> 6 != UNICODE_MARKER_CONTINUATION
							&& b4 >> 6 != UNICODE_MARKER_CONTINUATION)
					{
						fis.unread(b4);
						fis.unread(b3);
						fis.unread(b2);
					}
				}

				positions.put(new Position(line, column), p++);

				if (b == '\n')
				{
					line++;
					column = 0;
				}

				column++;

			}

			return TypedResult.ok(positions);

		}
		catch (IOException e)
		{
			return TypedResult.fail(e.getMessage());
		}
	}

	public static TypedResult<CharacterMap> mapCharacters(File targetFile)
	{
		try (FileInputStream fis = new FileInputStream(targetFile))
		{
			return mapCharactersFromStream(fis);
		}
		catch (IOException e)
		{
			return TypedResult.fail(e.getMessage());
		}
	}

	public static TypedResult<CharacterMap> mapCharacters(String input)
	{
		try (ByteArrayInputStream bis = new ByteArrayInputStream(input.getBytes()))
		{
			return mapCharactersFromStream(bis);
		}
		catch (IOException e)
		{
			return TypedResult.fail(e.getMessage());
		}
	}

	private static TypedResult<CharacterMap> mapCharactersFromStream(InputStream fis)
			throws IOException
	{
		CharacterMap characters = new CharacterMap();

		int b;
		int pos = 1;

		while ((b = fis.read()) != -1)
		{
			char c = (char) b;

			characters.put(c, pos++);

		}

		return TypedResult.ok(characters);
	}

	public static class FileMap extends TreeMap<Position, Integer>
	{

		private static final long serialVersionUID = -1777458988888895089L;

		@Override
		public Integer get(Object key)
		{
			Integer integer = super.get(key);
			return integer;
		}
	}

	public static class CharacterMap
	{
		private final TreeMultimap<Character, Location> characterPositions;

		private CharacterMap()
		{
			characterPositions = TreeMultimap.create(Comparator.naturalOrder(),
				Comparator.comparing(Location::getStart).thenComparing(Location::getEnd));
		}

		private void put(char c, int pos)
		{
			characterPositions.put(c, new Location(pos, pos + 1));
		}

		public SortedSet<Location> findIn(char target, Location location)
		{
			return characterPositions.get(target)
				.stream()
				.filter(l -> l.getStart() >= location.getStart())
				.filter(l -> l.getEnd() <= location.getEnd())
				.collect(Collectors.toCollection(TreeSet::new));
		}

		public SortedSet<Location> findBetweenInclusive(char target, Location from, Location to)
		{
			return characterPositions.get(target)
				.stream()
				.filter(l -> l.getStart() >= from.getStart())
				.filter(l -> l.getEnd() <= to.getEnd() + 1)
				.collect(Collectors.toCollection(TreeSet::new));
		}

		public SortedSet<Location> findBetweenExclusive(char target, Location from, Location to)
		{
			return characterPositions.get(target)
				.stream()
				.filter(l -> l.getStart() >= from.getStart())
				.filter(l -> l.getEnd() <= to.getStart())
				.collect(Collectors.toCollection(TreeSet::new));
		}
	}

}
