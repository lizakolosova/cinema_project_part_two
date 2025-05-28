import confetti from "canvas-confetti";

export function doConfetti() {
    confetti({
        particleCount: 100,
        spread: 60,
        origin: { x: 0.2, y: 0.6 }
    });
    confetti({
        particleCount: 100,
        spread: 60,
        origin: { x: 0.8, y: 0.6 }
    });
}
