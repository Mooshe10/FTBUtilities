package com.feed_the_beast.ftbutilities.cmd.tp;

import com.feed_the_beast.ftblib.lib.cmd.CmdBase;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftbutilities.FTBUtilitiesLang;
import com.feed_the_beast.ftbutilities.data.FTBUtilitiesPlayerData;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CmdDelHome extends CmdBase
{
	public CmdDelHome()
	{
		super("delhome", Level.ALL);
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		if (args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args, FTBUtilitiesPlayerData.get(Universe.get().getPlayer(sender)).homes.list());
		}

		return super.getTabCompletions(server, sender, args, pos);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		FTBUtilitiesPlayerData data = FTBUtilitiesPlayerData.get(getForgePlayer(sender));

		if (args.length == 0)
		{
			args = new String[] {"home"};
		}

		args[0] = args[0].toLowerCase();

		if (data.homes.set(args[0], null))
		{
			FTBUtilitiesLang.HOME_DEL.sendMessage(sender, args[0]);
			data.player.markDirty();
		}
		else
		{
			throw FTBUtilitiesLang.HOME_NOT_SET.commandError(args[0]);
		}
	}
}