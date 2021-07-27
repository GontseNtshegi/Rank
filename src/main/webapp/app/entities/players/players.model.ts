import { IPlayersAudit } from 'app/entities/players-audit/players-audit.model';

export interface IPlayers {
  id?: number;
  currentBalance?: number | null;
  wageringMoney?: number | null;
  winningMoney?: number | null;
  username?: string | null;
  email?: string | null;
  contact?: string | null;
  playerIds?: IPlayersAudit[] | null;
}

export class Players implements IPlayers {
  constructor(
    public id?: number,
    public currentBalance?: number | null,
    public wageringMoney?: number | null,
    public winningMoney?: number | null,
    public username?: string | null,
    public email?: string | null,
    public contact?: string | null,
    public playerIds?: IPlayersAudit[] | null
  ) {}
}

export function getPlayersIdentifier(players: IPlayers): number | undefined {
  return players.id;
}
