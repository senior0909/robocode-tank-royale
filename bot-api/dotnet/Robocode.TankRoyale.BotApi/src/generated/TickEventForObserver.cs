//----------------------
// <auto-generated>
//     Generated using the NJsonSchema v10.7.1.0 (Newtonsoft.Json v9.0.0.0) (http://NJsonSchema.org)
// </auto-generated>
//----------------------


namespace Robocode.TankRoyale.Schema
{
    #pragma warning disable // Disable all warnings

    /// <summary>
    /// Event occurring for before each new turn in the battle. Gives details for observers.
    /// </summary>
    [System.CodeDom.Compiler.GeneratedCode("NJsonSchema", "10.7.1.0 (Newtonsoft.Json v9.0.0.0)")]
    public class TickEventForObserver : Event
    {
        /// <summary>
        /// The current round number in the battle when event occurred
        /// </summary>
        [Newtonsoft.Json.JsonProperty("roundNumber", Required = Newtonsoft.Json.Required.Always)]
        public int RoundNumber { get; set; }

        /// <summary>
        /// Current state of all bots
        /// </summary>
        [Newtonsoft.Json.JsonProperty("botStates", Required = Newtonsoft.Json.Required.Always)]
        [System.ComponentModel.DataAnnotations.Required]
        public System.Collections.Generic.ICollection<BotStateWithId> BotStates { get; set; } = new System.Collections.ObjectModel.Collection<BotStateWithId>();

        /// <summary>
        /// Current state of all bullets
        /// </summary>
        [Newtonsoft.Json.JsonProperty("bulletStates", Required = Newtonsoft.Json.Required.Always)]
        [System.ComponentModel.DataAnnotations.Required]
        public System.Collections.Generic.ICollection<BulletState> BulletStates { get; set; } = new System.Collections.ObjectModel.Collection<BulletState>();

        /// <summary>
        /// All events occurring at this tick
        /// </summary>
        [Newtonsoft.Json.JsonProperty("events", Required = Newtonsoft.Json.Required.Always)]
        [System.ComponentModel.DataAnnotations.Required]
        public System.Collections.Generic.ICollection<Event> Events { get; set; } = new System.Collections.ObjectModel.Collection<Event>();


    }
}